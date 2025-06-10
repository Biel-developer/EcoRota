export default function Features() {
  return (
    <section className="section features" id="features">
      <div className="container">
        <div className="section-header">
          <span className="section-tag">Recursos</span>
          <h2 className="section-title">Tecnologia a serviço da eficiência</h2>
          <p className="section-subtitle">Conheça os principais recursos do EchoRota</p>
        </div>
        <div className="features-grid">
          <div className="feature-card" data-aos="fade-up">
            <div className="feature-icon">
              <i className="fas fa-route"></i>
            </div>
            <h3>Otimização de rota inteligente</h3>
            <p>
              Utiliza algoritmos avançados de otimização para criar rotas mais eficientes, reduzindo tempo e custos.
            </p>
          </div>
          <div className="feature-card" data-aos="fade-up" data-aos-delay="200">
            <div className="feature-icon">
              <i className="fas fa-map-marked-alt"></i>
            </div>
            <h3>Rastreamento de frota em tempo real</h3>
            <p>Monitore a posição exata dos seus veículos de coleta e otimize rotas em tempo real.</p>
          </div>
          <div className="feature-card" data-aos="fade-up" data-aos-delay="400">
            <div className="feature-icon">
              <i className="fas fa-chart-line"></i>
            </div>
            <h3>Gestão de resíduos orientada por dados</h3>
            <p>Análises e insights baseados em dados para melhorar continuamente o processo de coleta.</p>
          </div>
          <div className="feature-card" data-aos="fade-up" data-aos-delay="200">
            <div className="feature-icon">
              <i className="fas fa-calendar-alt"></i>
            </div>
            <h3>Planejamento inteligente</h3>
            <p>Crie programações otimizadas para coleta, considerando horários de tráfego e outras variáveis.</p>
          </div>
          <div className="feature-card" data-aos="fade-up" data-aos-delay="400">
            <div className="feature-icon">
              <i className="fas fa-mobile-alt"></i>
            </div>
            <h3>Aplicativo móvel para motoristas</h3>
            <p>Interface intuitiva para motoristas seguirem rotas otimizadas e reportarem ocorrências.</p>
          </div>
          <div className="feature-card" data-aos="fade-up" data-aos-delay="600">
            <div className="feature-icon">
              <i className="fas fa-tachometer-alt"></i>
            </div>
            <h3>Dashboard de desempenho</h3>
            <p>Visualize métricas importantes e acompanhe o desempenho da operação em tempo real.</p>
          </div>
        </div>
      </div>
    </section>
  )
}
