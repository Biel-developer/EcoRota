-- Inserir dados de exemplo para Maringá-PR

-- Nós principais da cidade (intersecões importantes e depósitos)
INSERT INTO no_grafo (latitude, longitude, tipo) VALUES
-- Depósito central (garagem municipal)
(-23.4180, -51.9350, 'DEPOSITO'),
-- Centro da cidade
(-23.4205, -51.9331, 'INTERSECAO'),
-- Zona Norte
(-23.4100, -51.9300, 'INTERSECAO'),
(-23.4050, -51.9280, 'INTERSECAO'),
-- Zona Sul
(-23.4300, -51.9400, 'INTERSECAO'),
(-23.4350, -51.9420, 'INTERSECAO'),
-- Zona Leste
(-23.4250, -51.9200, 'INTERSECAO'),
(-23.4270, -51.9150, 'INTERSECAO'),
-- Zona Oeste
(-23.4150, -51.9450, 'INTERSECAO'),
(-23.4120, -51.9500, 'INTERSECAO'),
-- Pontos de coleta específicos
(-23.4110, -51.9310, 'PONTO_COLETA'),
(-23.4320, -51.9390, 'PONTO_COLETA'),
(-23.4260, -51.9210, 'PONTO_COLETA'),
(-23.4140, -51.9460, 'PONTO_COLETA'),
(-23.4200, -51.9340, 'PONTO_COLETA');

-- Pontos de coleta
INSERT INTO ponto_de_coleta (no_grafo_id, nome, tipo_lixo, prioridade, status) VALUES
(11, 'Conjunto Habitacional Requião', 'ORGANICO', 1, 'PENDENTE'),
(12, 'Jardim Alvorada - Setor Residencial', 'RECICLAVEL', 2, 'PENDENTE'),
(13, 'Vila Esperança - Comercial', 'ORGANICO', 1, 'PENDENTE'),
(14, 'Jardim Universitário - UEM', 'RECICLAVEL', 3, 'PENDENTE'),
(15, 'Centro - Zona Comercial', 'ESPECIAL', 1, 'PENDENTE');

-- Trechos principais da malha viária
INSERT INTO trecho (no_origem_id, no_destino_id, distancia_metros, tempo_estimado_segundos, sentido_unico, velocidade_media_permitida) VALUES
-- Do depósito para as zonas principais
(1, 2, 2500, 300, FALSE, 50), -- Depósito -> Centro
(2, 3, 3200, 384, FALSE, 50), -- Centro -> Zona Norte
(2, 5, 2800, 336, FALSE, 50), -- Centro -> Zona Sul
(2, 7, 3500, 350, FALSE, 60), -- Centro -> Zona Leste (Av. Colombo)
(2, 9, 4200, 504, FALSE, 50), -- Centro -> Zona Oeste

-- Conexões entre zonas
(3, 4, 1800, 216, FALSE, 50), -- Zona Norte interna
(3, 7, 2200, 220, FALSE, 60), -- Norte -> Leste
(5, 6, 1600, 192, FALSE, 50), -- Zona Sul interna
(7, 8, 2000, 240, FALSE, 50), -- Zona Leste interna
(9, 10, 2400, 288, FALSE, 50), -- Zona Oeste interna

-- Conexões para pontos de coleta
(3, 11, 800, 120, FALSE, 40),  -- Zona Norte -> Ponto Coleta 1
(5, 12, 900, 135, FALSE, 40),  -- Zona Sul -> Ponto Coleta 2
(7, 13, 600, 90, FALSE, 40),   -- Zona Leste -> Ponto Coleta 3
(9, 14, 700, 105, FALSE, 40),  -- Zona Oeste -> Ponto Coleta 4
(2, 15, 500, 75, FALSE, 30);   -- Centro -> Ponto Coleta 5

-- Caminhões da frota municipal
INSERT INTO caminhao (placa, capacidade_kg, capacidade_volume_m3, localizacao_atual_latitude, localizacao_atual_longitude, status) VALUES
('MGA-0001', 8000.00, 25.00, -23.4180, -51.9350, 'DISPONIVEL'),
('MGA-0002', 6000.00, 20.00, -23.4180, -51.9350, 'DISPONIVEL'),
('MGA-0003', 10000.00, 35.00, -23.4180, -51.9350, 'MANUTENCAO'),
('MGA-0004', 7500.00, 22.50, -23.4180, -51.9350, 'DISPONIVEL');
