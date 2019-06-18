package io.gazetteer.tilesource.mbtiles;

import io.gazetteer.tilesource.Tile;
import io.gazetteer.tilesource.TileException;
import io.gazetteer.tilesource.TileReader;
import io.gazetteer.tilesource.TileWriter;
import io.gazetteer.tilesource.XYZ;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class SQLiteTileStore implements TileReader, TileWriter {

  public final org.sqlite.SQLiteDataSource dataSource;

  public final Map<String, String> metadata;

  public SQLiteTileStore(org.sqlite.SQLiteDataSource dataSource, Map<String, String> metadata) {
    this(dataSource, metadata, 10000);
  }

  public SQLiteTileStore(
      org.sqlite.SQLiteDataSource dataSource, Map<String, String> metadata, int cacheSize) {
    this.dataSource = dataSource;
    this.metadata = metadata;

  }

  @Override
  public Tile read(XYZ xyz) throws TileException {
    try (Connection connection = dataSource.getConnection()) {
      return SQLiteUtil.getTile(connection, xyz);
    } catch (SQLException e) {
      throw new TileException(e);
    }
  }

  public static SQLiteTileStore fromDataSource(org.sqlite.SQLiteDataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      Map<String, String> metadata = SQLiteUtil.getMetadata(connection);
      return new SQLiteTileStore(dataSource, metadata);
    }
  }

  @Override
  public void write(XYZ xyz, Tile tile) throws TileException {
    try (Connection connection = dataSource.getConnection()) {
      SQLiteUtil.setTile(connection, xyz, tile);
    } catch (SQLException e) {
      throw new TileException(e);
    }
  }
}