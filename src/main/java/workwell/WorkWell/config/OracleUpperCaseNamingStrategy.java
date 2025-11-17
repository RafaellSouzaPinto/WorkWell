package workwell.WorkWell.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class OracleUpperCaseNamingStrategy extends PhysicalNamingStrategyStandardImpl {

	@Override
	public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
		return applyUpperCase(name, context);
	}

	@Override
	public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
		return applyUpperCase(name, context);
	}

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		return applyUpperCase(name, context);
	}

	@Override
	public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
		return applyUpperCase(name, context);
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
		return applyUpperCase(name, context);
	}

	private Identifier applyUpperCase(Identifier name, JdbcEnvironment context) {
		if (name == null) {
			return null;
		}
		// Sempre converte para maiúsculas e remove aspas para Oracle
		// Isso garante que o Oracle trate como maiúsculas (sem case-sensitive)
		return Identifier.toIdentifier(name.getText().toUpperCase(), false);
	}
}

