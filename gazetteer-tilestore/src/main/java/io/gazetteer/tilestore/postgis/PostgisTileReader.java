package io.gazetteer.tilestore.postgis;

import io.gazetteer.tilestore.model.Tile;
import io.gazetteer.tilestore.model.TileException;
import io.gazetteer.tilestore.model.TileReader;
import io.gazetteer.tilestore.model.XYZ;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.dbcp2.PoolingDataSource;

public class PostgisTileReader implements TileReader {

  private final PoolingDataSource datasource;

  private final List<PostgisLayer> layers;

  public PostgisTileReader(PoolingDataSource datasource, List<PostgisLayer> layers) {
    this.datasource = datasource;
    this.layers = layers;
  }

  @Override
  public Tile read(XYZ xyz) throws TileException {
    try (Connection connection = datasource.getConnection();
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        GZIPOutputStream tile = new GZIPOutputStream(data)) {
      for (PostgisLayer layer : layers) {
        if (xyz.getZ() >= layer.getMinZoom() && xyz.getZ() <= layer.getMaxZoom()) {
          String sql = PostgisQueryBuilder.build(xyz, layer);
          try (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(sql);
            result.next();
            tile.write(result.getBytes(1));
          }
        }
      }
      tile.close();
      return new Tile(data.toByteArray());
    } catch (Exception e) {
      throw new TileException(e);
    }
  }

}