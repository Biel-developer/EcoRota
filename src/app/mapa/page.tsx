"use client";
import { useEffect, useState, useMemo } from "react";
import dynamic from "next/dynamic";
import "leaflet/dist/leaflet.css";
import "./mapa.css";

// Importação dinâmica dos componentes do react-leaflet (SSR off)
const MapContainer = dynamic(() => import("react-leaflet").then(m => m.MapContainer), { ssr: false });
const TileLayer = dynamic(() => import("react-leaflet").then(m => m.TileLayer), { ssr: false });
const Marker = dynamic(() => import("react-leaflet").then(m => m.Marker), { ssr: false });
const Polyline = dynamic(() => import("react-leaflet").then(m => m.Polyline), { ssr: false });
const Popup = dynamic(() => import("react-leaflet").then(m => m.Popup), { ssr: false });

const maringaCoords: [number, number] = [-23.4273, -51.9375];

type Caminhao = {
  id: number;
  placa: string;
  localizacaoAtualLatitude: number;
  localizacaoAtualLongitude: number;
  status: string;
};

type No = {
  id: number;
  latitude: number;
  longitude: number;
};

type Trecho = {
  id: number;
  noOrigemId: number;
  noDestinoId: number;
  sentidoUnico: boolean;
};

type PontoDeColeta = {
  id: number;
  latitude: number;
  longitude: number;
  nome?: string;
};

type Rota = {
  id: number;
  nome: string;
  trechos: Trecho[];
};

type OtimizarRotaRequest = {
  caminhaoId: number;
  tiposLixo: string[];
};

export default function MapaPage() {
  const [caminhoes, setCaminhoes] = useState<Caminhao[]>([]);
  const [nos, setNos] = useState<No[]>([]);
  const [trechos, setTrechos] = useState<Trecho[]>([]);
  const [pontos, setPontos] = useState<PontoDeColeta[]>([]);
  const [rotas, setRotas] = useState<Rota[]>([]);
  const [rotaOtimizada, setRotaOtimizada] = useState<Rota | null>(null);
  const [loadingOtimizar, setLoadingOtimizar] = useState(false);
  const [erroOtimizar, setErroOtimizar] = useState<string | null>(null);
  const [L, setL] = useState<any>(null);

  useEffect(() => {
    fetch("http://localhost:8082/api/caminhoes").then(r => r.json()).then(setCaminhoes);
    fetch("http://localhost:8082/api/nos-grafo").then(r => r.json()).then(setNos);
    fetch("http://localhost:8082/api/trechos").then(r => r.json()).then(setTrechos);
    fetch("http://localhost:8082/api/pontos-coleta").then(r => r.json()).then(setPontos);
    fetch("http://localhost:8082/api/rotas").then(r => r.json()).then(setRotas);

    // Importa o Leaflet só no client
    import("leaflet").then((leaflet) => setL(leaflet));
  }, []);

  const getNoCoords = (id: number) => {
    const no = nos.find(n => n.id === id);
    return no ? [no.latitude, no.longitude] as [number, number] : null;
  };

  // Cria os ícones customizados só quando L estiver disponível
  const caminhaoIcon = useMemo(() => {
    if (!L) return undefined;
    return L.divIcon({
      html: '<i class="fas fa-truck"></i>',
      className: 'custom-div-icon truck-icon',
      iconSize: [30, 30],
      iconAnchor: [15, 15]
    });
  }, [L]);

  const pontoIcon = useMemo(() => {
    if (!L) return undefined;
    return L.divIcon({
      html: '<i class="fas fa-trash"></i>',
      className: 'custom-div-icon trash-icon',
      iconSize: [20, 20],
      iconAnchor: [10, 10]
    });
  }, [L]);

  // Função para chamar a rota de otimização
  const otimizarRota = async () => {
    setLoadingOtimizar(true);
    setErroOtimizar(null);
    try {
      const caminhaoId = caminhoes[0]?.id;
      if (!caminhaoId) throw new Error("Nenhum caminhão disponível.");
      const tiposLixo = ["ORGANICO", "RECICLAVEL", "ESPECIAL"];
      const req: OtimizarRotaRequest = { caminhaoId, tiposLixo };
      const resp = await fetch("http://localhost:8082/api/rotas/otimizar", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(req),
      });
      if (!resp.ok) throw new Error("Falha ao otimizar rota");
      const data = await resp.json();
      setRotaOtimizada(data);
    } catch (e: any) {
      setErroOtimizar(e.message);
    } finally {
      setLoadingOtimizar(false);
    }
  };

  // Só renderiza o mapa se L já foi carregado (evita erro de SSR)
  if (!L) return null;

  return (
    <div className="main-container">
      <div className="map-container">
        <button
          onClick={otimizarRota}
          disabled={loadingOtimizar}
          style={{ position: "absolute", zIndex: 1000, top: 10, left: 10 }}
        >
          {loadingOtimizar ? "Otimizando..." : "Otimizar Rota"}
        </button>
        {erroOtimizar && (
          <div style={{ color: "red", position: "absolute", zIndex: 1000, top: 50, left: 10 }}>
            {erroOtimizar}
          </div>
        )}
        <MapContainer center={maringaCoords} zoom={13} style={{ height: "100vh", width: "100%" }}>
          <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
          {/* Caminhões */}
          {caminhoes.map(c => (
            <Marker
              key={c.id}
              position={[c.localizacaoAtualLatitude, c.localizacaoAtualLongitude]}
              icon={caminhaoIcon}
            >
              <Popup>
                Caminhão {c.placa}<br />
                Status: {c.status}
              </Popup>
            </Marker>
          ))}
          {/* Pontos de coleta */}
          {pontos.map(p => (
            <Marker
              key={p.id}
              position={[p.latitude, p.longitude]}
              icon={pontoIcon}
            >
              <Popup>
                {p.nome || `Ponto ${p.id}`}
              </Popup>
            </Marker>
          ))}
          {/* Nós */}
          {nos.map(no => (
            <Marker
              key={no.id}
              position={[no.latitude, no.longitude]}
            >
              <Popup>Nó {no.id}</Popup>
            </Marker>
          ))}
          {/* Trechos */}
          {trechos.map(trecho => {
            const origem = getNoCoords(trecho.noOrigemId);
            const destino = getNoCoords(trecho.noDestinoId);
            if (!origem || !destino) return null;
            return (
              <Polyline
                key={trecho.id}
                positions={[origem, destino]}
                pathOptions={{ color: trecho.sentidoUnico ? "red" : "blue" }}
              />
            );
          })}
          {/* Rotas (opcional: desenhar cada rota com cor diferente) */}
          {rotas.map(rota => (
            <Polyline
              key={rota.id}
              positions={rota.trechos
                .map(t => {
                  const origem = getNoCoords(t.noOrigemId);
                  const destino = getNoCoords(t.noDestinoId);
                  return origem && destino ? [origem, destino] : null;
                })
                .flat()
                .filter(Boolean) as [number, number][]}
              pathOptions={{ color: "#ff9800", weight: 3 }}
            />
          ))}
          {/* Rota Otimizada */}
          {rotaOtimizada && rotaOtimizada.trechos && (
            <Polyline
              positions={rotaOtimizada.trechos
                .map(t => {
                  const origem = getNoCoords(t.noOrigemId);
                  const destino = getNoCoords(t.noDestinoId);
                  return origem && destino ? [origem, destino] : null;
                })
                .flat()
                .filter(Boolean) as [number, number][]}
              pathOptions={{ color: "#00e676", weight: 5 }}
            />
          )}
        </MapContainer>
      </div>
    </div>
  );
}