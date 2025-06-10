-- Tabela para armazenar todos os nós geográficos do grafo (interseções, pontos de coleta, depósitos)
CREATE TABLE no_grafo (
    id BIGSERIAL PRIMARY KEY,
    latitude NUMERIC(10, 7) NOT NULL,
    longitude NUMERIC(10, 7) NOT NULL,
    tipo VARCHAR(50) NOT NULL, -- Ex: 'INTERSECAO', 'PONTO_COLETA', 'DEPOSITO'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Índices para otimizar buscas por localização e tipo
CREATE INDEX idx_no_grafo_location ON no_grafo (latitude, longitude);
CREATE INDEX idx_no_grafo_tipo ON no_grafo (tipo);

-- Tabela para armazenar os pontos de coleta de lixo
CREATE TABLE ponto_de_coleta (
    id BIGSERIAL PRIMARY KEY,
    no_grafo_id BIGINT NOT NULL, -- Chave estrangeira para o nó geográfico correspondente
    nome VARCHAR(255) NOT NULL,
    tipo_lixo VARCHAR(50) NOT NULL, -- Ex: 'ORGANICO', 'RECICLAVEL', 'ESPECIAL'
    prioridade INTEGER NOT NULL DEFAULT 1, -- 1 (mais alta) a N (mais baixa)
    status VARCHAR(50) NOT NULL DEFAULT 'PENDENTE', -- Ex: 'PENDENTE', 'COLETADO', 'IGNORADO'
    data_ultima_coleta TIMESTAMP WITH TIME ZONE,
    observacoes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_no_grafo
        FOREIGN KEY (no_grafo_id)
        REFERENCES no_grafo(id)
        ON DELETE RESTRICT -- Não permite deletar um nó se houver um ponto de coleta associado
);

-- Índices para otimizar buscas e junções
CREATE INDEX idx_ponto_de_coleta_status ON ponto_de_coleta (status);
CREATE INDEX idx_ponto_de_coleta_prioridade ON ponto_de_coleta (prioridade);
CREATE INDEX idx_ponto_de_coleta_no_grafo_id ON ponto_de_coleta (no_grafo_id);


-- Tabela para armazenar os caminhões de coleta
CREATE TABLE caminhao (
    id BIGSERIAL PRIMARY KEY,
    placa VARCHAR(20) UNIQUE NOT NULL,
    capacidade_kg NUMERIC(10, 2) NOT NULL,
    capacidade_volume_m3 NUMERIC(10, 2) NOT NULL,
    localizacao_atual_latitude NUMERIC(10, 7),
    localizacao_atual_longitude NUMERIC(10, 7),
    status VARCHAR(50) NOT NULL DEFAULT 'DISPONIVEL', -- Ex: 'DISPONIVEL', 'EM_ROTA', 'MANUTENCAO'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Índice para otimizar buscas por placa
CREATE INDEX idx_caminhao_placa ON caminhao (placa);


-- Tabela para armazenar os trechos (arestas) da malha viária
CREATE TABLE trecho (
    id BIGSERIAL PRIMARY KEY,
    no_origem_id BIGINT NOT NULL, -- Chave estrangeira para o nó de origem
    no_destino_id BIGINT NOT NULL, -- Chave estrangeira para o nó de destino
    distancia_metros NUMERIC(10, 2) NOT NULL,
    tempo_estimado_segundos NUMERIC(10, 2) NOT NULL,
    sentido_unico BOOLEAN NOT NULL DEFAULT FALSE, -- TRUE se for mão única (origem -> destino)
    velocidade_media_permitida NUMERIC(5, 2), -- Km/h ou m/s, dependendo da unidade
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_no_origem
        FOREIGN KEY (no_origem_id)
        REFERENCES no_grafo(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_no_destino
        FOREIGN KEY (no_destino_id)
        REFERENCES no_grafo(id)
        ON DELETE RESTRICT,

    -- Garante que não haja trechos duplicados na mesma direção
    CONSTRAINT uq_trecho_origem_destino UNIQUE (no_origem_id, no_destino_id)
);

-- Índices para otimizar buscas e junções
CREATE INDEX idx_trecho_origem_id ON trecho (no_origem_id);
CREATE INDEX idx_trecho_destino_id ON trecho (no_destino_id);


-- Tabela para armazenar as rotas otimizadas geradas
CREATE TABLE rota_otimizada (
    id BIGSERIAL PRIMARY KEY,
    caminhao_id BIGINT NOT NULL, -- Chave estrangeira para o caminhão que fará a rota
    data_geracao TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    distancia_total_metros NUMERIC(12, 2) NOT NULL,
    tempo_total_estimado_segundos NUMERIC(12, 2) NOT NULL,
    -- Armazena a sequência de IDs dos nós (no_grafo_id) na rota, em formato JSONB
    sequencia_nos JSONB NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ATIVA', -- Ex: 'ATIVA', 'CONCLUIDA', 'CANCELADA'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_caminhao
        FOREIGN KEY (caminhao_id)
        REFERENCES caminhao(id)
        ON DELETE RESTRICT
);

-- Índices para otimizar buscas
CREATE INDEX idx_rota_otimizada_caminhao_id ON rota_otimizada (caminhao_id);
CREATE INDEX idx_rota_otimizada_data_geracao ON rota_otimizada (data_geracao);
CREATE INDEX idx_rota_otimizada_status ON rota_otimizada (status);

-- Função para atualizar automaticamente a coluna updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Gatilhos (triggers) para as tabelas
CREATE TRIGGER update_no_grafo_updated_at
BEFORE UPDATE ON no_grafo
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_ponto_de_coleta_updated_at
BEFORE UPDATE ON ponto_de_coleta
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_caminhao_updated_at
BEFORE UPDATE ON caminhao
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_trecho_updated_at
BEFORE UPDATE ON trecho
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_rota_otimizada_updated_at
BEFORE UPDATE ON rota_otimizada
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();