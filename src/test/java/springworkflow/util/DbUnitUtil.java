package springworkflow.util;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;

public class DbUnitUtil {

	private static DataSource dataSource = null;

	private DbUnitUtil() {
	}

	public static void setUpDbUnitUtil(DataSource dataSource) {
		DbUnitUtil.dataSource = dataSource;
	}

	public static Connection getConnection() throws Exception {
		if (dataSource == null) {
			throw new Exception("setUpDbUtilが実行されていません");
		}
		return dataSource.getConnection();
	}

	public static void clearTableData(String... tableName) throws Exception {
		IDatabaseConnection idcon = null;
		try (Connection con = getConnection();) {

			// IDatabaseConnectionの作成
			idcon = new DatabaseConnection(con);

			// レコードを削除するテーブルのデータセットの取得
			IDataSet dataset = idcon.createDataSet(tableName);

			// データの全件削除
			con.setAutoCommit(false);
			DatabaseOperation.DELETE_ALL.execute(idcon, dataset);
			con.commit();
			con.setAutoCommit(true);
		} finally {
			if (idcon != null) {
				idcon.close();
			}
		}
	}

	public static void insertData(String path) throws Exception {
		IDatabaseConnection idcon = null;
		try (Connection con = getConnection();) {

			// IDatabaseConnectionの作成
			idcon = new DatabaseConnection(con);
			DatabaseConfig config = idcon.getConfig();
			config.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
			config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
			config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());

			// 投入するテストデータファイルからデータセットを作成
			// ここにはフルパスにするためにtestも追加する
			IDataSet dataset = new CsvDataSet(new File("src/test/resources/" + path));

			// 初期データの投入
			con.setAutoCommit(false);
			DatabaseOperation.INSERT.execute(idcon, dataset);
			con.commit();
			con.setAutoCommit(true);
		} finally {
			if (idcon != null) {
				idcon.close();
			}
		}
	}

	public static SortedTable getExpectedTable(String path, String tableName, String[] sortKey) throws Exception {
		// 指定ファイルにおけるデータセットを取得
		// ここにはフルパスにするためにtestも追加する
		IDataSet dataset = new CsvDataSet(new File("src/test/resources/" + path));

		// 期待するテーブルの状態の取得
		ITable expected = dataset.getTable(tableName);

		// 比較のためITableオブジェクト内のデータをsortKeyでソート
		SortedTable sortedExpected = new SortedTable(expected, sortKey);

		return sortedExpected;
	}

	public static SortedTable getActualTable(String tableName, String[] sortKey) throws Exception {
		SortedTable sortedActual = null;
		IDatabaseConnection idcon = null;
		try (Connection con = getConnection();) {

			// IDatabaseConnectionの作成
			idcon = new DatabaseConnection(con);
			DatabaseConfig config = idcon.getConfig();
			config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
			config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());

			// 実際のテーブルの状態の取得
			ITable actual = idcon.createDataSet().getTable(tableName);

			// 比較のためITableオブジェクト内のデータをsortKeyでソート
			sortedActual = new SortedTable(actual, sortKey);
		} finally {
			if (idcon != null) {
				idcon.close();
			}
		}
		return sortedActual;
	}

	public static void renameTable(String before, String after) throws Exception {
		String renameTable = "RENAME TABLE " + before + " TO " + after;
		try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(renameTable);) {
			ps.executeUpdate();
		}
	}
}