"use client"

import { useEffect, useState } from "react"
import dynamic from "next/dynamic"
import { useMap } from "react-leaflet"
import { LatLngExpression } from "leaflet";
import './globals.css'
import { useRouter } from "next/navigation"; 

 
const MapContainer = dynamic(() => import("react-leaflet").then(m => m.MapContainer), { ssr: false });
const TileLayer = dynamic(() => import("react-leaflet").then(m => m.TileLayer), { ssr: false });
const Marker = dynamic(() => import("react-leaflet").then(m => m.Marker), { ssr: false });
const Popup = dynamic(() => import("react-leaflet").then(m => m.Popup), { ssr: false });
const Polyline = dynamic(() => import("react-leaflet").then(m => m.Polyline), { ssr: false });

import "leaflet/dist/leaflet.css"

const markerIcon = (color: string) => {
  const L = require("leaflet");
  return new L.Icon({
    iconUrl: `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-${color}.png`,
    shadowUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png",
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41],
  });
};

const truckIcon = () => {
  const L = require("leaflet");
  return new L.Icon({
    iconUrl: "http://localhost:3000/image/caminhao.png", 
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -32],
    shadowUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png", 
    shadowSize: [41, 41],
    shadowAnchor: [12, 41],
  });
};

const trashIcon = () => {
  const L = require("leaflet");
  return new L.Icon({
    iconUrl: "https://img.icons8.com/fluency/48/trash.png",
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -32],
    shadowUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png",
    shadowSize: [41, 41],
    shadowAnchor: [12, 41],
  });
};

interface Caminhao {
  id: number
  placa: string
  modelo: string
  capacidadeKg: number
  nivelCombustivel: number
  latitude: number
  longitude: number
  status: string
}

interface NoGrafo {
  id: number
  latitude: number
  longitude: number
  tipo: string
}

interface Trecho {
  id: number
  noOrigemId: number
  noDestinoId: number
  distanciaMetros: number
  tempoEstimadoSegundos: number
  velocidadeMediaPermitida: number
}

interface PontoColeta {
  id: number
  nome: string
  latitude: number
  longitude: number
  tipoLixo: string
  volumeEstimado: number
  ultimaColeta: string
  prioridade: number
}

interface Rota {
  id: number
  caminhaoId: number
  pontos: number[]
  distanciaTotalMetros: number
  tempoTotalEstimadoSegundos: number
  status: string
  dataInicio: string
  dataFim: string
}

// ----------- INTEGRAÇÃO GRAPHHOPPER -----------
const GH_API_KEY = "1e733f5b-26fd-4f9f-92a6-6713ac01e8f8"; // Troque pela sua chave

async function buscarRotaGraphHopper(pontos: [number, number][]) {
  if (pontos.length < 2) return null;
  const url = `https://graphhopper.com/api/1/route?point=${pontos
    .map(([lat, lng]) => `${lat},${lng}`)
    .join("&point=")}&vehicle=car&locale=pt&points_encoded=false&key=${GH_API_KEY}`;
  const res = await fetch(url);
  const data = await res.json();
  console.log("Resposta GraphHopper:", data);
  if (data.paths && data.paths.length > 0) {
    // Converte [lng, lat] para [lat, lng]
    return data.paths[0].points.coordinates.map(
      ([lng, lat]: [number, number]) => [lat, lng]
    );
  }
  return null;
}
// ----------- FIM INTEGRAÇÃO GRAPHHOPPER -----------

export default function EcoRotaMap() {
  const [caminhoes, setCaminhoes] = useState<Caminhao[]>([])
  const [nosGrafo, setNosGrafo] = useState<NoGrafo[]>([])
  const [trechos, setTrechos] = useState<Trecho[]>([])
  const [pontosColeta, setPontosColeta] = useState<PontoColeta[]>([])
  const [rotas, setRotas] = useState<Rota[]>([])

  const [mostrarCaminhoes, setMostrarCaminhoes] = useState(true)
  const [mostrarPontosColeta, setMostrarPontosColeta] = useState(true)
  const [mostrarRotas, setMostrarRotas] = useState(true)
  const [rotaSelecionada, setRotaSelecionada] = useState<number | null>(null)
  const [caminhaoSelecionado, setCaminhaoSelecionado] = useState<number | null>(null)
  const [abaSelecionada, setAbaSelecionada] = useState("dashboard")
  
  const router = useRouter();

  useEffect(() => {
    if (typeof window !== "undefined") {
      const isLoggedIn = localStorage.getItem("isLoggedIn");
      if (isLoggedIn !== "true") {
        router.replace("/login"); 
      }
    }
  }, [router]);

  const [metricas, setMetricas] = useState({
    distanciaTotalMetros: 0,
    tempoTotalEstimadoSegundos: 0,
    combustivelEconomizado: 0,
    sequenciaNos: 0,
    eficiencia: 0,
  })

  const [carregando, setCarregando] = useState(true)
  const [otimizando, setOtimizando] = useState(false)

  const [caminhaoAnimadoPos, setCaminhaoAnimadoPos] = useState<LatLngExpression | null>(null);
  const [animando, setAnimando] = useState(false);
  const [intervalId, setIntervalId] = useState<NodeJS.Timeout | null>(null);

  const [rotaGraphHopper, setRotaGraphHopper] = useState<[number, number][]>([]);

  const posicaoInicial: [number, number] = [-23.420999, -51.933056]

  const buscarDados = async () => {
    setCarregando(true)
    try {
      const [resCaminhoes, resNosGrafo, resTrechos, resPontosColeta, resRotas] = await Promise.all([
        fetch("http://localhost:8081/api/caminhoes").then((res) => res.json()),
        fetch("http://localhost:8081/api/nos-grafo").then((res) => res.json()),
        fetch("http://localhost:8081/api/trechos").then((res) => res.json()),
        fetch("http://localhost:8081/api/pontos-coleta").then((res) => res.json()),
        fetch("http://localhost:8081/api/rotas").then((res) => res.json()),
      ])

      const caminhoesMapeados = resCaminhoes.map((c: any) => ({
        id: c.id,
        placa: c.placa,
        modelo: c.modelo ?? "",
        capacidadeKg: c.capacidadeKg,
        nivelCombustivel: c.nivelCombustivel ?? 100,
        latitude: c.latitude ?? c.localizacaoAtualLatitude,
        longitude: c.longitude ?? c.localizacaoAtualLongitude,
        status: c.status,
      }));

      const rotasMapeadas = resRotas.map((r: any) => ({
        ...r,
        pontos: r.pontos ?? r.sequenciaNos ?? [],
      }));

      setCaminhoes(caminhoesMapeados)
      setNosGrafo(resNosGrafo)
      setTrechos(resTrechos)
      setPontosColeta(resPontosColeta)
      setRotas(rotasMapeadas)

      calcularMetricas(rotasMapeadas, resPontosColeta)
    } catch (error) {
      console.error("Erro ao buscar dados:", error)
    } finally {
      setCarregando(false)
    }
  }

  const otimizarRotas = async () => {
    setOtimizando(true)
    try {
      const resRotasOtimizadas = await fetch("http://localhost:8081/api/rotas/otimizar", {
        method: "POST",
      }).then((res) => res.json())

      const rotasArray = Array.isArray(resRotasOtimizadas) ? resRotasOtimizadas : []

      setRotas(rotasArray)
      calcularMetricas(rotasArray, pontosColeta)
      setMostrarRotas(true)
    } catch (error) {
      console.error("Erro ao otimizar rotas:", error)
    } finally {
      setOtimizando(false)
    }
  }

  const calcularMetricas = (rotasData: Rota[], pontosData: PontoColeta[]) => {
    const distanciaTotalMetros = rotasData.reduce((acc, rota) => acc + rota.distanciaTotalMetros, 0)
    const tempoTotalEstimadoSegundos = rotasData.reduce((acc, rota) => acc + rota.tempoTotalEstimadoSegundos, 0)
    const sequenciaNos = new Set(rotasData.flatMap((rota) => Array.isArray(rota.pontos) ? rota.pontos : [])).size

    const distanciaTotalKm = distanciaTotalMetros / 3000

    const combustivelEconomizado = distanciaTotalKm * 0.10
    const eficiencia = distanciaTotalKm > 0 ? (sequenciaNos / distanciaTotalKm) * 100 : 0

    setMetricas({
      distanciaTotalMetros: distanciaTotalKm,
      tempoTotalEstimadoSegundos,
      combustivelEconomizado,
      sequenciaNos,
      eficiencia,
    })
  }

  const obterCoordenadas = (pontoId: number) => {
    const ponto = pontosColeta.find((p) => p.id === pontoId)
    if (ponto) {
      return [ponto.latitude, ponto.longitude]
    }

    const no = nosGrafo.find((n) => n.id === pontoId)
    if (no) {
      return [no.latitude, no.longitude]
    }

    return null
  }

  const construirCaminhoRota = (rota: Rota) => {
    if (!rota.pontos || !Array.isArray(rota.pontos)) return [];
    return rota.pontos
      .map((pontoId) => obterCoordenadas(pontoId))
      .filter((coord) => coord !== null) as [number, number][];
  };

  const animarCaminhao = (rota: Rota, caminhoCustomizado?: [number, number][]) => {
    if (!rota) {
      alert("Esta rota não possui pontos para animar.");
      return;
    }
    if (intervalId) {
      clearInterval(intervalId);
    }
    setAnimando(false);
    setCaminhaoAnimadoPos(null);

    const caminho = caminhoCustomizado && caminhoCustomizado.length > 1
      ? caminhoCustomizado
      : (rota.pontos
        .map((pontoId) => obterCoordenadas(pontoId))
        .filter((coord) => coord !== null) as [number, number][]);

    if (caminho.length === 0) return;

    setAnimando(true);
    let i = 0;
    setCaminhaoAnimadoPos(caminho[0]);

    const caminhaoId = rota.caminhaoId;

    const id = setInterval(() => {
      i++;
      if (i >= caminho.length) {
        clearInterval(id);
        setAnimando(false);
        setCaminhaoAnimadoPos(null);
        return;
      }
      setCaminhaoAnimadoPos(caminho[i]);
      setCaminhoes((prev) =>
        prev.map((c) =>
          c.id === caminhaoId
            ? {
              ...c,
              nivelCombustivel: Math.max(0, c.nivelCombustivel - 2),
            }
            : c
        )
      );
    }, 500);
    setIntervalId(id);
  };

  const traçarRotaGraphHopper = async (rota: Rota) => {
    if (!rota || !rota.pontos || rota.pontos.length < 2) {
      alert("Selecione uma rota com pelo menos dois pontos.");
      return;
    }
    let coords = rota.pontos
      .map((pontoId) => obterCoordenadas(pontoId))
      .filter((coord) => coord !== null) as [number, number][];

    coords = coords.filter(
      (coord, idx, arr) =>
        arr.findIndex(
          (c) => c[0] === coord[0] && c[1] === coord[1]
        ) === idx
    );

    if (coords.length >= 5) {
      alert("A rota tem muitos pontos para a API gratuita do GraphHopper (máx. 5). Mostrando apenas os 5 primeiros.");
      coords = coords.slice(0, 5);
    }

    if (coords.length < 2) {
      alert("Não foi possível obter as coordenadas dos pontos.");
      return;
    }
    console.log("Coordenadas enviadas para GraphHopper:", coords);
    const rotaGH = await buscarRotaGraphHopper(coords);
    if (rotaGH) setRotaGraphHopper(rotaGH);
    else alert("Não foi possível traçar a rota com o GraphHopper.");
  };

  useEffect(() => {
    buscarDados()

    const intervalo = setInterval(() => {
      buscarDados()
    }, 30000)

    return () => {
      clearInterval(intervalo);
      if (intervalId) {
        clearInterval(intervalId);
      }
    }
  }, [])

  const CentrarMapa = ({ caminhaoId }: { caminhaoId: number | null }) => {
    const map = useMap()

    useEffect(() => {
      if (caminhaoId !== null) {
        const caminhao = caminhoes.find((c) => c.id === caminhaoId)
        if (caminhao) {
          map.setView([caminhao.latitude, caminhao.longitude], 15)
        }
      }
    }, [caminhaoId, map, caminhoes])

    return null
  }

  const coresRotas = ["#FF5733", "#33FF57", "#3357FF", "#F033FF", "#FF33A8", "#33FFF6"]

  return (
    <div className="flex flex-col h-screen bg-gray-100">
      {/* Header */}
      <header className="bg-green-700 text-white p-4 shadow-lg">
        {/* ...header igual... */}
        <div className="container mx-auto flex justify-between items-center">
          <div className="flex items-center gap-3">
             <i className="fas fa-route"></i>
            <h1 className="text-3xl font-bold">EcoRota</h1>
            <span className="text-green-200 text-sm">Sistema de Otimização de Rotas</span>
          </div>
          <div className="flex items-center gap-4">
            <div className="bg-green-600 px-3 py-1 rounded-full text-sm">{caminhoes.length} Caminhões Ativos</div>
            <div className="bg-green-600 px-3 py-1 rounded-full text-sm">{pontosColeta.length} Pontos de Coleta</div>
            <button
              className="bg-green-600 hover:bg-green-800 px-4 py-2 rounded-lg flex items-center gap-2 transition-colors"
              onClick={buscarDados}
              disabled={carregando}
            >
              <svg
                className={`w-4 h-4 ${carregando ? "animate-spin" : ""}`}
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"
                />
              </svg>
              Atualizar
            </button>
          </div>
        </div>
      </header>
      <div className="flex flex-1 overflow-hidden">
        {/* Painel lateral */}
        <div className="w-1/4 bg-white shadow-lg overflow-y-auto">
          <div className="border-b">
            <div className="flex">
              {[
                { id: "dashboard", label: "Dashboard", icon: "📊" },
                { id: "caminhoes", label: "Frota", icon: "🚛" },
                { id: "pontos", label: "Pontos", icon: "🗑️" },
                { id: "rotas", label: "Rotas", icon: "🛣️" },
              ].map((tab) => (
                <button
                  key={tab.id}
                  className={`flex-1 px-4 py-3 text-sm font-medium border-b-2 transition-colors ${abaSelecionada === tab.id
                    ? "border-green-500 text-green-600 bg-green-50"
                    : "border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300"
                    }`}
                  onClick={() => setAbaSelecionada(tab.id)}
                >
                  <span className="mr-1">{tab.icon}</span>
                  {tab.label}
                </button>
              ))}
            </div>
          </div>

          <div className="p-4">
            {abaSelecionada === "dashboard" && (
              <div className="space-y-6">
                <div>
                  <h2 className="text-xl font-bold text-gray-800 mb-2">Desempenho Operacional</h2>
                  <p className="text-gray-600 text-sm mb-4">Métricas de eficiência da operação</p>

                  <div className="mb-6">
                    <div className="flex justify-between mb-2">
                      <span className="text-sm font-medium text-gray-700">Eficiência de Rota</span>
                      <span className="text-sm font-medium text-gray-700">{metricas.eficiencia.toFixed(1)}%</span>
                    </div>
                    <div className="w-full bg-gray-200 rounded-full h-2">
                      <div
                        className="bg-green-600 h-2 rounded-full transition-all duration-300"
                        style={{ width: `${Math.min(metricas.eficiencia, 100)}%` }}
                      ></div>
                    </div>
                  </div>

                  <div className="grid grid-cols-2 gap-4 mb-6">
                    <div className="bg-gray-50 p-4 rounded-lg">
                      <p className="text-sm text-gray-500 mb-1">Distância Total</p>
                      <p className="text-2xl font-bold text-gray-800">{metricas.distanciaTotalMetros.toFixed(1)} km</p>
                    </div>
                    <div className="bg-gray-50 p-4 rounded-lg">
                      <p className="text-sm text-gray-500 mb-1">Tempo Estimado</p>
                      <p className="text-2xl font-bold text-gray-800">{(metricas.tempoTotalEstimadoSegundos / 60).toFixed(1)} h</p>
                    </div>
                    <div className="bg-gray-50 p-4 rounded-lg">
                      <p className="text-sm text-gray-500 mb-1">Pontos Atendidos</p>
                      <p className="text-2xl font-bold text-gray-800">{metricas.sequenciaNos}</p>
                    </div>
                    <div className="bg-gray-50 p-4 rounded-lg">
                      <p className="text-sm text-gray-500 mb-1">Combustível Economizado</p>
                      <p className="text-2xl font-bold text-gray-800">{metricas.combustivelEconomizado.toFixed(1)} L</p>
                    </div>
                  </div>

                  {/* Botão de otimização */}
                  <button
                    className="w-full bg-green-700 hover:bg-green-800 text-white py-3 px-4 rounded-lg font-medium flex items-center justify-center gap-2 transition-colors disabled:opacity-50"
                    onClick={otimizarRotas}
                    disabled={otimizando}
                  >
                    <svg
                      className={`w-5 h-5 ${otimizando ? "animate-spin" : ""}`}
                      fill="none"
                      stroke="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"
                      />
                    </svg>
                    {otimizando ? "Otimizando..." : "Otimizar Rotas"}
                  </button>
                </div>
              </div>
            )}

            {abaSelecionada === "caminhoes" && (
              <div className="space-y-4">
                <div>
                  <h2 className="text-xl font-bold text-gray-800 mb-2">Frota de Caminhões</h2>
                  <p className="text-gray-600 text-sm mb-4">Status e localização em tempo real</p>
                </div>

                <div className="space-y-3 max-h-[60vh] overflow-y-auto">
                  {caminhoes.map((caminhao) => {
                    // Encontra a rota do caminhão
                    const rotaDoCaminhao = rotas.find((r) => r.caminhaoId === caminhao.id);

                    return (
                      <div
                        key={caminhao.id}
                        className={`p-4 border rounded-lg cursor-pointer transition-all ${caminhaoSelecionado === caminhao.id
                          ? "bg-green-50 border-green-500 shadow-md"
                          : "hover:bg-gray-50 border-gray-200"
                          }`}
                        onClick={() => setCaminhaoSelecionado(caminhao.id)}
                      >
                        <div className="flex justify-between items-center mb-3">
                          <div>
                            <p className="font-semibold text-gray-800">{caminhao.placa}</p>
                            <p className="text-sm text-gray-500">{caminhao.modelo}</p>
                          </div>
                          <span
                            className={`px-2 py-1 rounded-full text-xs font-medium ${caminhao.status === "ATIVO" ? "bg-green-100 text-green-800" : "bg-gray-100 text-gray-800"
                              }`}
                          >
                            {caminhao.status}
                          </span>
                        </div>
                        <div>
                          <div className="flex justify-between text-sm mb-1">
                            <span className="text-gray-600">Combustível</span>
                            <span className="font-medium">{caminhao.nivelCombustivel}%</span>
                          </div>
                          <div className="w-full bg-gray-200 rounded-full h-2">
                            <div
                              className={`h-2 rounded-full transition-all duration-300 ${caminhao.nivelCombustivel > 50
                                ? "bg-green-500"
                                : caminhao.nivelCombustivel > 25
                                  ? "bg-yellow-500"
                                  : "bg-red-50"
                                }`}
                              style={{ width: `${caminhao.nivelCombustivel}%` }}
                            ></div>
                          </div>
                        </div>
                        {/* Botão para animar caminhão da frota */}
                        <button
                          className="mt-3 w-full bg-green-500 hover:bg-green-600 text-white py-2 px-4 rounded-lg font-medium text-sm transition-colors disabled:opacity-50"
                          onClick={(e) => {
                            e.stopPropagation();
                            if (rotaDoCaminhao) {
                              setRotaSelecionada(rotaDoCaminhao.id);
                              animarCaminhao(rotaDoCaminhao, rotaGraphHopper.length > 1 ? rotaGraphHopper : undefined);
                            } else {
                              alert("Este caminhão não possui rota atribuída.");
                            }
                          }}
                          disabled={animando}
                        >
                          {animando && caminhaoSelecionado === caminhao.id ? "Animando..." : "Animar Caminhão"}
                        </button>
                      </div>
                    );
                  })}
                </div>

                <div className="flex gap-2 pt-4 border-t">
                  <button
                    className="flex-1 px-3 py-2 text-sm border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
                    onClick={() => setMostrarCaminhoes(!mostrarCaminhoes)}
                  >
                    {mostrarCaminhoes ? "Ocultar no Mapa" : "Mostrar no Mapa"}
                  </button>
                  <button
                    className="flex-1 px-3 py-2 text-sm border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
                    onClick={() => setCaminhaoSelecionado(null)}
                  >
                    Limpar Seleção
                  </button>
                </div>
              </div>
            )}

            {/* Pontos de Coleta */}
            {abaSelecionada === "pontos" && (
              <div className="space-y-4">
                <div>
                  <h2 className="text-xl font-bold text-gray-800 mb-2">Pontos de Coleta</h2>
                  <p className="text-gray-600 text-sm mb-4">Locais para coleta de resíduos</p>
                </div>

                <select className="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent">
                  <option value="TODOS">Todos os tipos</option>
                  <option value="ORGANICO">Orgânico</option>
                  <option value="RECICLAVEL">Reciclável</option>
                  <option value="PERIGOSO">Perigoso</option>
                </select>

                <div className="space-y-3 max-h-[50vh] overflow-y-auto">
                  {pontosColeta.map((ponto) => (
                    <div
                      key={ponto.id}
                      className="p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors"
                    >
                      <div className="flex justify-between items-center mb-2">
                        <p className="font-semibold text-gray-800">Ponto #{ponto.id}</p>
                        <span
                          className={`px-2 py-1 rounded-full text-xs font-medium ${ponto.prioridade > 7
                            ? "bg-red-100 text-red-800"
                            : ponto.prioridade > 4
                              ? "bg-yellow-100 text-yellow-800"
                              : "bg-green-100 text-green-800"
                            }`}
                        >
                          Prioridade {ponto.prioridade}
                        </span>
                      </div>
                      <p className="text-sm text-gray-600 mb-2">{ponto.nome}</p>
                      <div className="flex justify-between text-sm">
                        <span className="text-gray-600">{ponto.tipoLixo}</span>
                        <span className="font-medium">{ponto.volumeEstimado} kg</span>
                      </div>
                    </div>
                  ))}
                </div>

                <button
                  className="w-full px-3 py-2 text-sm border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
                  onClick={() => setMostrarPontosColeta(!mostrarPontosColeta)}
                >
                  {mostrarPontosColeta ? "Ocultar no Mapa" : "Mostrar no Mapa"}
                </button>
              </div>
            )}

            {/* Rotas */}
            {abaSelecionada === "rotas" && (
              <div className="space-y-4">
                <div>
                  <h2 className="text-xl font-bold text-gray-800 mb-2">Rotas de Coleta</h2>
                  <p className="text-gray-600 text-sm mb-4">Planejamento de percursos</p>
                </div>

                <div className="space-y-3 max-h-[60vh] overflow-y-auto">
                  {rotas.map((rota, index) => (
                    <div
                      key={rota.id}
                      className={`p-4 border rounded-lg cursor-pointer transition-all ${rotaSelecionada === rota.id
                        ? "bg-green-50 border-green-500 shadow-md"
                        : "hover:bg-gray-50 border-gray-200"
                        }`}
                      onClick={() => setRotaSelecionada(rota.id)}
                    >
                      <div className="flex justify-between items-center mb-3">
                        <div className="flex items-center gap-2">
                          <div
                            className="w-4 h-4 rounded-full"
                            style={{ backgroundColor: coresRotas[index % coresRotas.length] }}
                          />
                          <p className="font-semibold text-gray-800">Rota #{rota.id}</p>
                        </div>
                        <span
                          className={`px-2 py-1 rounded-full text-xs font-medium ${rota.status === "CONCLUIDA"
                            ? "bg-green-100 text-green-800"
                            : rota.status === "EM_ANDAMENTO"
                              ? "bg-blue-100 text-blue-800"
                              : "bg-gray-100 text-gray-800"
                            }`}
                        >
                          {rota.status}
                        </span>
                      </div>
                      <div className="space-y-1 text-sm">
                        <div className="flex justify-between">
                          <span className="text-gray-600">Caminhão</span>
                          <span className="font-medium">#{rota.caminhaoId}</span>
                        </div>
                        <div className="flex justify-between">
                          <span className="text-gray-600">Pontos de coleta</span>
                          <span className="font-medium">{Array.isArray(rota.pontos) ? rota.pontos.length : 0}</span>
                        </div>
                        <div className="flex justify-between">
                          <span className="text-gray-600">Distância</span>
                          <span className="font-medium">
                            {typeof rota.distanciaTotalMetros === "number" ? rota.distanciaTotalMetros.toFixed(1) : "0.0"} km
                          </span>
                        </div>
                        <div className="flex justify-between">
                          <span className="text-gray-600">Tempo estimado</span>
                          <span className="font-medium">{(rota.tempoTotalEstimadoSegundos / 60).toFixed(1)} h</span>
                        </div>
                      </div>
                      {/* Botão para animar o caminhão */}
                      <button
                        className="mt-3 w-full bg-green-500 hover:bg-green-600 text-white py-2 px-4 rounded-lg font-medium text-sm transition-colors disabled:opacity-50"
                        onClick={(e) => {
                          e.stopPropagation();
                          setRotaSelecionada(rota.id);
                          animarCaminhao(rota);
                        }}
                        disabled={animando && rotaSelecionada !== rota.id}
                      >
                        {animando && rotaSelecionada === rota.id ? "Animando..." : "Animar Caminhão"}
                      </button>
                      {/* Botão para traçar rota GraphHopper */}
                      <button
                        className="mt-2 w-full bg-purple-600 hover:bg-purple-800 text-white py-2 px-4 rounded-lg font-medium text-sm transition-colors"
                        onClick={(e) => {
                          e.stopPropagation();
                          traçarRotaGraphHopper(rota);
                        }}
                      >
                        Traçar rota GraphHopper
                      </button>
                    </div>
                  ))}
                </div>

                <div className="flex gap-2 pt-4 border-t">
                  <button
                    className="flex-1 px-3 py-2 text-sm border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
                    onClick={() => setMostrarRotas(!mostrarRotas)}
                  >
                    {mostrarRotas ? "Ocultar no Mapa" : "Mostrar no Mapa"}
                  </button>
                  <button
                    className="flex-1 px-3 py-2 text-sm border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
                    onClick={() => setRotaSelecionada(null)}
                  >
                    Limpar Seleção
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>

        {/* Mapa */}
        <div className="flex-1 relative">
          <MapContainer center={posicaoInicial} zoom={13} style={{ height: "100%", width: "100%" }}>
            <TileLayer
              attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />

            {/* Caminhões no mapa */}
            {mostrarCaminhoes &&
              caminhoes
                .filter(
                  (caminhao) =>
                    typeof caminhao.latitude === "number" &&
                    typeof caminhao.longitude === "number" &&
                    !isNaN(caminhao.latitude) &&
                    !isNaN(caminhao.longitude)
                )
                .map((caminhao) => (
                  <Marker
                    key={`caminhao-${caminhao.id}`}
                    position={[caminhao.latitude, caminhao.longitude]}
                    icon={truckIcon()}
                  >
                    <Popup>
                      <div className="p-2">
                        <h3 className="font-bold text-lg mb-2">🚛 Caminhão {caminhao.placa}</h3>
                        <div className="space-y-1 text-sm">
                          <p>
                            <strong>Modelo:</strong> {caminhao.modelo}
                          </p>
                          <p>
                            <strong>Placa</strong> {caminhao.placa}
                          </p>
                          <p>
                            <strong>Status:</strong> {caminhao.status}
                          </p>
                          <p>
                            <strong>Combustível:</strong> {caminhao.nivelCombustivel}%
                          </p>
                          <p>
                            <strong>Capacidade:</strong> {caminhao.capacidadeKg}kg
                          </p>
                        </div>
                      </div>
                    </Popup>
                  </Marker>
                ))}

            {/* Pontos de coleta no mapa */}
            {mostrarPontosColeta &&
              pontosColeta
                .filter(
                  (ponto) =>
                    typeof ponto.latitude === "number" &&
                    typeof ponto.longitude === "number" &&
                    !isNaN(ponto.latitude) &&
                    !isNaN(ponto.longitude)
                )
                .map((ponto) => (
                  <Marker
                    key={`ponto-${ponto.id}`}
                    position={[ponto.latitude, ponto.longitude]}
                    icon={trashIcon()}
                  >
                    <Popup>
                      <div className="p-2">
                        <h3 className="font-bold text-lg mb-2">🗑️ Ponto de Coleta #{ponto.id}</h3>
                        <div className="space-y-1 text-sm">
                          <p>
                            <strong>Endereço:</strong> {ponto.nome}
                          </p>
                          <p>
                            <strong>Tipo:</strong> {ponto.tipoLixo}
                          </p>
                          <p>
                            <strong>Volume:</strong> {ponto.volumeEstimado} kg
                          </p>
                          <p>
                            <strong>Prioridade:</strong> {ponto.prioridade}/10
                          </p>
                          <p>
                            <strong>Última coleta: não identificado</strong> {ponto.ultimaColeta}
                          </p>
                        </div>
                      </div>
                    </Popup>
                  </Marker>
                ))}

            {/* Rotas no mapa
            {mostrarRotas &&
              rotas.map((rota, index) => {
                const caminho = construirCaminhoRota(rota)
                const cor = coresRotas[index % coresRotas.length]

                return (
                  <Polyline
                    key={`rota-${rota.id}`}
                    positions={caminho}
                    pathOptions={{
                      color: cor,
                      weight: rotaSelecionada === rota.id ? 6 : 3,
                      opacity: rotaSelecionada === rota.id ? 1 : 0.7,
                    }}
                  />
                )
              })} */}

            {/* Rota GraphHopper desenhada no mapa */}
            {rotaGraphHopper.length > 0 && (
              <Polyline
                positions={rotaGraphHopper}
                pathOptions={{ color: "#8000ff", weight: 5, opacity: 0.8 }}
              />
            )}

            {/* Exibe o caminhão animado se estiver animando */}
            {animando && caminhaoAnimadoPos && (
              <Marker position={caminhaoAnimadoPos} icon={truckIcon()}>
                <Popup>Caminhão em movimento</Popup>
              </Marker>
            )}

            {/* Componente para centralizar o mapa */}
            <CentrarMapa caminhaoId={caminhaoSelecionado} />
          </MapContainer>

          {/* Controles do mapa */}
          <div className="absolute bottom-4 right-4 bg-white p-3 rounded-lg shadow-lg z-[1000] flex flex-col gap-2">
            <button
              className="bg-green-600 hover:bg-green-800 text-white px-4 py-2 rounded transition"
              onClick={() => setMostrarCaminhoes((v) => !v)}
            >
              {mostrarCaminhoes ? "Esconder Caminhões" : "Mostrar Caminhões"}
            </button>
            <button
              className="bg-blue-600 hover:bg-blue-800 text-white px-4 py-2 rounded transition"
              onClick={() => setMostrarRotas((v) => !v)}
            >
              {mostrarRotas ? "Esconder Rotas" : "Mostrar Rotas"}
            </button>
            <button
              className="bg-yellow-600 hover:bg-yellow-800 text-white px-4 py-2 rounded transition"
              onClick={() => setMostrarPontosColeta((v) => !v)}
            >
              {mostrarPontosColeta ? "Esconder Pontos" : "Mostrar Pontos"}
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}