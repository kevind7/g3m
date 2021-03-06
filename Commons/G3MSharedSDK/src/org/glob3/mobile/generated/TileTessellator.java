package org.glob3.mobile.generated; 
public abstract class TileTessellator
{
  public void dispose()
  {
  }

  public abstract boolean isReady(G3MRenderContext rc);

  public abstract Mesh createTileMesh(Planet planet, Vector2I resolution, Tile tile, ElevationData elevationData, float verticalExaggeration, boolean mercator, boolean debug, TileTessellatorMeshData data);

  public abstract Vector2I getTileMeshResolution(Planet planet, Vector2I resolution, Tile tile, boolean debug);

  public abstract Mesh createTileDebugMesh(Planet planet, Vector2I resolution, Tile tile);

  public abstract IFloatBuffer createTextCoords(Vector2I resolution, Tile tile, boolean mercator);

  public Vector2F getTextCoord(Tile tile, Geodetic2D position, boolean mercator)
  {
    return getTextCoord(tile, position._latitude, position._longitude, mercator);
  }

  public abstract Vector2F getTextCoord(Tile tile, Angle latitude, Angle longitude, boolean mercator);

  public abstract void setRenderedSector(Sector sector);

}