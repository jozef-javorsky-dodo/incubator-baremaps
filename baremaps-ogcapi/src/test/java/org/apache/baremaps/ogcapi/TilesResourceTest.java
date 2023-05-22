/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.apache.baremaps.ogcapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

class TilesResourceTest extends OgcApiTest {

  @Test
  void getTileSets() {
    var response = target().path("/tiles").request().get();
    var body = response.readEntity(String.class);
    assertEquals(200, response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
    assertTrue(body.contains("default"));
  }

  @Test
  void getTileSet() {
    var response = target().path("/tiles/default").request().get();
    var body = response.readEntity(String.class);
    assertEquals(200, response.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
    assertTrue(body.contains("default"));
  }
}