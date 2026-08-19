import React, { useState, useEffect, useRef } from 'react';
import { Search, Play, Pause, Heart, Radio } from 'lucide-react';

interface Station {
  stationuuid: string;
  name: string;
  url_resolved: string;
  favicon: string;
  country: string;
  language: string;
  votes: number;
}

export default function App() {
  const [stations, setStations] = useState<Station[]>([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [activeStation, setActiveStation] = useState<Station | null>(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const audioRef = useRef<HTMLAudioElement | null>(null);

  useEffect(() => {
    fetchTopStations();
    audioRef.current = new Audio();
    audioRef.current.addEventListener('play', () => setIsPlaying(true));
    audioRef.current.addEventListener('pause', () => setIsPlaying(false));
    
    return () => {
      if (audioRef.current) {
        audioRef.current.pause();
        audioRef.current.src = '';
      }
    };
  }, []);

  const fetchTopStations = async () => {
    setIsLoading(true);
    try {
      const response = await fetch('https://all.api.radio-browser.info/json/stations/topclick?limit=50&hidebroken=true');
      const data = await response.json();
      setStations(data);
    } catch (error) {
      console.error('Error fetching stations:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const searchStations = async (query: string) => {
    if (!query) return fetchTopStations();
    
    setIsLoading(true);
    try {
      const response = await fetch(`https://all.api.radio-browser.info/json/stations/search?name=${encodeURIComponent(query)}&limit=50&hidebroken=true`);
      const data = await response.json();
      setStations(data);
    } catch (error) {
      console.error('Error searching stations:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const playStation = (station: Station) => {
    if (activeStation?.stationuuid === station.stationuuid) {
      if (isPlaying) {
        audioRef.current?.pause();
      } else {
        audioRef.current?.play().catch(e => console.error("Play failed", e));
      }
    } else {
      setActiveStation(station);
      if (audioRef.current) {
        audioRef.current.src = station.url_resolved;
        audioRef.current.play().catch(e => console.error("Play failed", e));
      }
    }
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-100 flex flex-col pb-24">
      <header className="p-4 bg-slate-900 border-b border-slate-800 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Radio className="w-6 h-6 text-cyan-500" />
          <h1 className="text-xl font-bold">Babu's Radio Services</h1>
        </div>
      </header>

      <main className="flex-1 p-4 max-w-3xl mx-auto w-full">
        <div className="relative mb-6">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 w-5 h-5" />
          <input
            type="text"
            placeholder="Search global stations..."
            className="w-full bg-slate-900 border border-slate-800 rounded-xl py-3 pl-10 pr-4 focus:outline-none focus:border-cyan-500 transition-colors"
            value={searchQuery}
            onChange={(e) => {
              setSearchQuery(e.target.value);
              const timeout = setTimeout(() => searchStations(e.target.value), 500);
              return () => clearTimeout(timeout);
            }}
          />
        </div>

        {isLoading ? (
          <div className="flex justify-center items-center py-12">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-cyan-500"></div>
          </div>
        ) : (
          <div className="grid gap-3">
            {stations.map(station => (
              <div
                key={station.stationuuid}
                className={`p-4 rounded-xl flex items-center gap-4 cursor-pointer transition-colors ${
                  activeStation?.stationuuid === station.stationuuid
                    ? 'bg-cyan-950 border border-cyan-900'
                    : 'bg-slate-900 border border-slate-800 hover:bg-slate-800'
                }`}
                onClick={() => playStation(station)}
              >
                {station.favicon ? (
                  <img src={station.favicon} alt="" className="w-12 h-12 rounded-lg bg-slate-800 object-cover" />
                ) : (
                  <div className="w-12 h-12 rounded-lg bg-slate-800 flex items-center justify-center">
                    <Radio className="w-6 h-6 text-slate-500" />
                  </div>
                )}
                
                <div className="flex-1 min-w-0">
                  <h3 className="font-bold truncate text-slate-100">{station.name || 'Unknown Station'}</h3>
                  <p className="text-sm text-slate-400 truncate">
                    {[station.country, station.language].filter(Boolean).join(' • ')}
                  </p>
                </div>

                <button className="p-2 rounded-full hover:bg-slate-800 transition-colors">
                  {activeStation?.stationuuid === station.stationuuid && isPlaying ? (
                    <Pause className="w-6 h-6 text-cyan-400" />
                  ) : (
                    <Play className="w-6 h-6 text-slate-400 group-hover:text-cyan-400" />
                  )}
                </button>
              </div>
            ))}
          </div>
        )}
      </main>

      {/* Persistent Bottom Player */}
      {activeStation && (
        <div className="fixed bottom-0 left-0 right-0 bg-slate-900 border-t border-slate-800 p-4 shadow-lg">
          <div className="max-w-3xl mx-auto flex items-center gap-4">
            {activeStation.favicon ? (
              <img src={activeStation.favicon} alt="" className="w-12 h-12 rounded-full bg-slate-800 object-cover" />
            ) : (
              <div className="w-12 h-12 rounded-full bg-slate-800 flex items-center justify-center">
                <Radio className="w-6 h-6 text-slate-500" />
              </div>
            )}
            
            <div className="flex-1 min-w-0">
              <h3 className="font-bold truncate text-slate-100">{activeStation.name}</h3>
              <p className="text-sm text-cyan-500 truncate">{isPlaying ? 'Now Playing' : 'Paused'}</p>
            </div>

            <button 
              onClick={() => playStation(activeStation)}
              className="w-12 h-12 rounded-full bg-cyan-500 text-slate-950 flex items-center justify-center hover:bg-cyan-400 transition-colors"
            >
              {isPlaying ? <Pause className="w-6 h-6" /> : <Play className="w-6 h-6 ml-1" />}
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
