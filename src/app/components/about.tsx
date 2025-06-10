export default function About() {
  return (
    <section className="section about" id="about">
      <div className="container">
        <div className="section-header">
          <span className="section-tag">Sobre o Projeto</span>
          <h2 className="section-title">Otimização inteligente de rotas</h2>
          <p className="section-subtitle">Utilizando técnicas de geoprocessamento e algoritmos de otimização</p>
        </div>
        <div className="about-content">
          <div className="about-image" data-aos="fade-right">
            <img src="/image/caminhaoLixo.png" alt="Caminhão de coleta de lixo" />
            <div className="image-shape"></div>
          </div>
          <div className="about-text" data-aos="fade-left">
            <h3>Transformando a coleta de resíduos urbanos</h3>
            <p>
              O EcoRota é um sistema inovador que utiliza tecnologia avançada para otimizar as rotas de coleta de lixo
              em áreas urbanas. Nosso objetivo é tornar o processo mais eficiente, reduzindo custos operacionais e
              minimizando o impacto ambiental.
            </p>
            <ul className="about-list">
              <li>
                <span className="list-icon">
                  <i className="fas fa-map-marked-alt"></i>
                </span>
                <div className="list-content">
                  <h4>Mapeamento detalhado</h4>
                  <p>Análise completa da geografia urbana e pontos de coleta</p>
                </div>
              </li>
              <li>
                <span className="list-icon">
                  <i className="fas fa-code-branch"></i>
                </span>
                <div className="list-content">
                  <h4>Algoritmos avançados</h4>
                  <p>Cálculo de rotas otimizadas baseado em múltiplas variáveis</p>
                </div>
              </li>
              <li>
                <span className="list-icon">
                  <i className="fas fa-tachometer-alt"></i>
                </span>
                <div className="list-content">
                  <h4>Monitoramento em tempo real</h4>
                  <p>Acompanhamento da frota e ajustes dinâmicos de rota</p>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </section>
  )
}
