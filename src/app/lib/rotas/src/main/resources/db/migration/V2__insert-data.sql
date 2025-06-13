
-- Dados aprimorados para Maringá-PR com mais nós e trechos das principais avenidas

-- Limpar dados existentes (opcional)
-- DELETE FROM rota_otimizada;
-- DELETE FROM ponto_de_coleta;
-- DELETE FROM trecho;
-- DELETE FROM caminhao;
-- DELETE FROM no_grafo;

-- Nós principais das avenidas de Maringá
INSERT INTO no_grafo (latitude, longitude, tipo) VALUES
-- Depósito central (garagem municipal) - Centro
(-23.4180, -51.9350, 'DEPOSITO'),

-- Avenida Colombo (principal via leste-oeste)
(-23.4205, -51.9331, 'INTERSECAO'), -- Centro
(-23.4200, -51.9250, 'INTERSECAO'), -- Av. Colombo - Zona 1
(-23.4195, -51.9200, 'INTERSECAO'), -- Av. Colombo - Zona 2
(-23.4190, -51.9150, 'INTERSECAO'), -- Av. Colombo - UEM
(-23.4210, -51.9400, 'INTERSECAO'), -- Av. Colombo - Zona Oeste
(-23.4215, -51.9450, 'INTERSECAO'), -- Av. Colombo - Zona Oeste 2

-- Avenida Herval (norte-sul)
(-23.4100, -51.9300, 'INTERSECAO'), -- Zona Norte
(-23.4150, -51.9320, 'INTERSECAO'), -- Zona Norte-Centro
(-23.4250, -51.9340, 'INTERSECAO'), -- Zona Sul
(-23.4300, -51.9360, 'INTERSECAO'), -- Zona Sul 2

-- Avenida Cerro Azul (norte-sul)
(-23.4080, -51.9280, 'INTERSECAO'), -- Zona Norte
(-23.4130, -51.9290, 'INTERSECAO'), -- Zona Norte-Centro
(-23.4280, -51.9320, 'INTERSECAO'), -- Zona Sul

-- Avenida Morangueira (leste-oeste)
(-23.4350, -51.9200, 'INTERSECAO'), -- Zona Sul-Leste
(-23.4345, -51.9250, 'INTERSECAO'), -- Zona Sul
(-23.4340, -51.9300, 'INTERSECAO'), -- Zona Sul-Centro
(-23.4335, -51.9350, 'INTERSECAO'), -- Zona Sul-Oeste

-- Pontos de coleta específicos
(-23.4110, -51.9310, 'PONTO_COLETA'), -- Zona Norte
(-23.4320, -51.9390, 'PONTO_COLETA'), -- Zona Sul
(-23.4260, -51.9210, 'PONTO_COLETA'), -- Zona Leste
(-23.4140, -51.9460, 'PONTO_COLETA'), -- Zona Oeste
(-23.4200, -51.9340, 'PONTO_COLETA'), -- Centro
(-23.4170, -51.9180, 'PONTO_COLETA'), -- Zona 7 - UEM
(-23.4380, -51.9280, 'PONTO_COLETA'), -- Zona Sul - Morangueira
(-23.4050, -51.9350, 'PONTO_COLETA'); -- Zona Norte - Herval

-- Pontos de coleta
INSERT INTO ponto_de_coleta (no_grafo_id, nome, tipo_lixo, prioridade, status) VALUES
(16, 'Conjunto Habitacional Requião', 'ORGANICO', 1, 'PENDENTE'),
(17, 'Jardim Alvorada - Setor Residencial', 'RECICLAVEL', 2, 'PENDENTE'),
(18, 'Vila Esperança - Comercial', 'ORGANICO', 1, 'PENDENTE'),
(19, 'Jardim Universitário - Zona Oeste', 'RECICLAVEL', 3, 'PENDENTE'),
(20, 'Centro - Zona Comercial', 'ESPECIAL', 1, 'PENDENTE'),
(21, 'Zona 7 - UEM Campus', 'RECICLAVEL', 2, 'PENDENTE'),
(22, 'Morangueira - Zona Sul', 'ORGANICO', 2, 'PENDENTE'),
(23, 'Zona Norte - Herval', 'ORGANICO', 1, 'PENDENTE');

-- Trechos das principais avenidas
INSERT INTO trecho (no_origem_id, no_destino_id, distancia_metros, tempo_estimado_segundos, sentido_unico, velocidade_media_permitida) VALUES
-- Avenida Colombo (leste-oeste)
(2, 3, 1200, 120, FALSE, 60), -- Centro -> Zona 1
(3, 4, 1300, 130, FALSE, 60), -- Zona 1 -> Zona 2
(4, 5, 1100, 110, FALSE, 60), -- Zona 2 -> UEM
(2, 6, 1500, 180, FALSE, 50), -- Centro -> Zona Oeste
(6, 7, 1200, 144, FALSE, 50), -- Zona Oeste -> Zona Oeste 2

-- Avenida Herval (norte-sul)
(8, 9, 1400, 168, FALSE, 50), -- Zona Norte -> Norte-Centro
(9, 2, 1200, 144, FALSE, 50), -- Norte-Centro -> Centro
(2, 10, 1300, 156, FALSE, 50), -- Centro -> Zona Sul
(10, 11, 1400, 168, FALSE, 50), -- Zona Sul -> Zona Sul 2

-- Avenida Cerro Azul (norte-sul)
(12, 13, 1300, 156, FALSE, 50), -- Zona Norte -> Norte-Centro
(13, 2, 1100, 132, FALSE, 50), -- Norte-Centro -> Centro
(2, 14, 1500, 180, FALSE, 50), -- Centro -> Zona Sul

-- Avenida Morangueira (leste-oeste)
(15, 16, 1200, 144, FALSE, 50), -- Sul-Leste -> Sul
(16, 17, 1300, 156, FALSE, 50), -- Sul -> Sul-Centro
(17, 18, 1100, 132, FALSE, 50), -- Sul-Centro -> Sul-Oeste

-- Conexões entre avenidas principais
(3, 9, 800, 96, FALSE, 40),   -- Colombo-Zona1 -> Herval Norte-Centro
(4, 13, 700, 84, FALSE, 40),  -- Colombo-Zona2 -> Cerro Azul Norte-Centro
(10, 16, 900, 108, FALSE, 40), -- Herval Sul -> Morangueira Sul
(14, 17, 850, 102, FALSE, 40), -- Cerro Azul Sul -> Morangueira Sul-Centro

-- Conexões para pontos de coleta
(8, 16, 600, 90, FALSE, 30),   -- Zona Norte -> Ponto Coleta 1
(10, 17, 700, 105, FALSE, 30), -- Zona Sul -> Ponto Coleta 2
(4, 18, 500, 75, FALSE, 30),   -- Zona Leste -> Ponto Coleta 3
(6, 19, 800, 120, FALSE, 30),  -- Zona Oeste -> Ponto Coleta 4
(2, 20, 300, 45, FALSE, 20),   -- Centro -> Ponto Coleta 5
(5, 21, 400, 60, FALSE, 30),   -- UEM -> Ponto Coleta 6
(15, 22, 600, 90, FALSE, 30),  -- Morangueira -> Ponto Coleta 7
(8, 23, 500, 75, FALSE, 30);   -- Zona Norte -> Ponto Coleta 8

-- Caminhões da frota municipal (atualizados)
INSERT INTO caminhao (placa, capacidade_kg, capacidade_volume_m3, localizacao_atual_latitude, localizacao_atual_longitude, status) VALUES
('MGA-0001', 8000.00, 25.00, -23.4180, -51.9350, 'DISPONIVEL'),
('MGA-0002', 6000.00, 20.00, -23.4180, -51.9350, 'DISPONIVEL'),
('MGA-0003', 10000.00, 35.00, -23.4180, -51.9350, 'MANUTENCAO'),
('MGA-0004', 7500.00, 22.50, -23.4180, -51.9350, 'DISPONIVEL');
