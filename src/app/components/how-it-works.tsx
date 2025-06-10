export default function HowItWorks() {
  return (
    <section className="section how-it-works">
      <div className="container">
        <div className="section-header">
          <span className="section-tag">Processo</span>
          <h2 className="section-title">Como funciona</h2>
          <p className="section-subtitle">Um processo simples e eficiente para otimizar suas rotas</p>
        </div>
        <div className="steps">
          <div className="step" data-aos="fade-right">
            <div className="step-number">01</div>
            <div className="step-content">
              <h3>Coleta de dados</h3>
              <p>
                Mapeamento detalhado da cidade, análise de padrões de geração de resíduos e estudo das rotas atuais.
              </p>
            </div>
          </div>
          <div className="step" data-aos="fade-right" data-aos-delay="200">
            <div className="step-number">02</div>
            <div className="step-content">
              <h3>Processamento</h3>
              <p>
                Aplicação de algoritmos de otimização para calcular as rotas mais eficientes considerando múltiplas
                variáveis.
              </p>
            </div>
          </div>
          <div className="step" data-aos="fade-right" data-aos-delay="400">
            <div className="step-number">03</div>
            <div className="step-content">
              <h3>Implementação</h3>
              <p>
                Integração com sistemas de navegação dos veículos e treinamento das equipes para seguir as novas rotas
                otimizadas.
              </p>
            </div>
          </div>
          <div className="step" data-aos="fade-right" data-aos-delay="600">
            <div className="step-number">04</div>
            <div className="step-content">
              <h3>Monitoramento</h3>
              <p>Acompanhamento contínuo do desempenho e ajustes para garantir a máxima eficiência do sistema.</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  )
}
