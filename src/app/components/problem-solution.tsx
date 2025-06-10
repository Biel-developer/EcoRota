export default function ProblemSolution() {
  return (
    <section className="section problem-solution">
      <div className="container">
        <div className="problem-solution-grid">
          <div className="problem-box" data-aos="fade-right">
            <div className="box-header">
              <div className="box-icon problem-icon">
                <i className="fas fa-exclamation-triangle"></i>
              </div>
              <h3>O Problema</h3>
            </div>
            <div className="box-content">
              <ul className="problem-list">
                <li>
                  <i className="fas fa-times-circle"></i>
                  <span>Rotas ineficientes e não otimizadas</span>
                </li>
                <li>
                  <i className="fas fa-times-circle"></i>
                  <span>Alto consumo de combustível</span>
                </li>
                <li>
                  <i className="fas fa-times-circle"></i>
                  <span>Maior desgaste dos veículos</span>
                </li>
                <li>
                  <i className="fas fa-times-circle"></i>
                  <span>Custos operacionais elevados</span>
                </li>
                <li>
                  <i className="fas fa-times-circle"></i>
                  <span>Impacto ambiental desnecessário</span>
                </li>
              </ul>
            </div>
          </div>
          <div className="solution-box" data-aos="fade-left">
            <div className="box-header">
              <div className="box-icon solution-icon">
                <i className="fas fa-lightbulb"></i>
              </div>
              <h3>Nossa Solução</h3>
            </div>
            <div className="box-content">
              <ul className="solution-list">
                <li>
                  <i className="fas fa-check-circle"></i>
                  <span>Rotas inteligentes e otimizadas</span>
                </li>
                <li>
                  <i className="fas fa-check-circle"></i>
                  <span>Redução no consumo de combustível</span>
                </li>
                <li>
                  <i className="fas fa-check-circle"></i>
                  <span>Menor desgaste da frota</span>
                </li>
                <li>
                  <i className="fas fa-check-circle"></i>
                  <span>Economia significativa de recursos</span>
                </li>
                <li>
                  <i className="fas fa-check-circle"></i>
                  <span>Menor impacto ambiental</span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </section>
  )
}
