export default function Benefits() {
  return (
    <section className="section benefits" id="benefits">
      <div className="container">
        <div className="section-header">
          <span className="section-tag">Benefícios</span>
          <h2 className="section-title">Vantagens do EchoRota</h2>
          <p className="section-subtitle">Transformando a gestão de resíduos urbanos</p>
        </div>
        <div className="benefits-grid">
          <div className="benefit-card" data-aos="zoom-in">
            <div className="benefit-icon">
              <i className="fas fa-clock"></i>
            </div>
            <h3>Otimização de tempo</h3>
            <p>Redução significativa no tempo necessário para completar as rotas de coleta.</p>
          </div>
          <div className="benefit-card" data-aos="zoom-in" data-aos-delay="100">
            <div className="benefit-icon">
              <i className="fas fa-dollar-sign"></i>
            </div>
            <h3>Economia de recursos</h3>
            <p>Menor gasto com combustível, manutenção de veículos e horas de trabalho.</p>
          </div>
          <div className="benefit-card" data-aos="zoom-in" data-aos-delay="200">
            <div className="benefit-icon">
              <i className="fas fa-leaf"></i>
            </div>
            <h3>Sustentabilidade</h3>
            <p>Redução na emissão de CO₂ e outros poluentes devido à otimização das rotas.</p>
          </div>
          <div className="benefit-card" data-aos="zoom-in" data-aos-delay="300">
            <div className="benefit-icon">
              <i className="fas fa-city"></i>
            </div>
            <h3>Cidades mais limpas</h3>
            <p>Maior eficiência na coleta contribui para ruas mais limpas e organizadas.</p>
          </div>
          <div className="benefit-card" data-aos="zoom-in" data-aos-delay="400">
            <div className="benefit-icon">
              <i className="fas fa-users"></i>
            </div>
            <h3>Satisfação da população</h3>
            <p>Serviço de coleta mais eficiente e regular, resultando em maior satisfação dos cidadãos.</p>
          </div>
          <div className="benefit-card" data-aos="zoom-in" data-aos-delay="500">
            <div className="benefit-icon">
              <i className="fas fa-truck"></i>
            </div>
            <h3>Otimização da frota</h3>
            <p>Melhor utilização dos veículos disponíveis, possibilitando atender mais áreas com a mesma frota.</p>
          </div>
        </div>
      </div>
    </section>
  )
}
