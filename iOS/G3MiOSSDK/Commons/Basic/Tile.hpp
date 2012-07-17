//
//  Tile.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_Tile_h
#define G3MiOSSDK_Tile_h

#include "Sector.hpp"
#include "Box.h"

class RenderContext;
class Mesh;
class TileTessellator;
class TileTexturizer;
class TileParameters;
class TilesCache;

//class TileKey {
//public:
//  const int _level;
//  const int _row;
//  const int _column;
//  
//  TileKey(const int level,
//          const int row,
//          const int column) :
//  _level(level),
//  _row(row),
//  _column(column)
//  {
//    
//  }
//  
//  TileKey(const TileKey& that):
//  _level(that._level),
//  _row(that._row),
//  _column(that._column)
//  {
//    
//  }
//  
//};


class Tile {
private:
  const Sector _sector;
  const int    _level;
  const int    _row;
  const int    _column;

  Mesh* _tessellatorMesh;
  Mesh* _texturizerMesh;

  Tile* _fallbackTextureTile;
  bool _textureSolved;
  
  inline Mesh* getTessellatorMesh(const RenderContext* rc,
                                  const TileTessellator* tessellator);
  
  inline bool isVisible(const RenderContext* rc,
                        const TileTessellator *tessellator);
  
  inline bool meetsRenderCriteria(const RenderContext* rc,
                                  const TileParameters* parameters);
  
  inline std::vector<Tile*>* createSubTiles(TilesCache* tilesCache,
                                            bool useCache);
  
  inline void rawRender(const RenderContext* rc,
                        const TileTessellator* tessellator,
                        TileTexturizer* texturizer);
  
  inline Tile* createSubTile(TilesCache* tilesCache,
                             const Angle& lowerLat, const Angle& lowerLon,
                             const Angle& upperLat, const Angle& upperLon,
                             const int level,
                             const int row, const int column,
                             Tile* fallbackTextureTile,
                             bool useCache);
  
  std::vector<Tile*>* _subtiles;
  inline std::vector<Tile*>* getSubTiles(TilesCache *tilesCache);

  inline void prune();
  
public:
  Tile(const Sector& sector,
       int level,
       int row,
       int column,
       Tile* fallbackTextureTile):
  _sector(sector),
  _level(level),
  _row(row),
  _column(column),
  _tessellatorMesh(NULL),
  _texturizerMesh(NULL),
  _textureSolved(false),
  _fallbackTextureTile(fallbackTextureTile),
//  _bbox(NULL),
  _subtiles(NULL)
  {
  }
  
  ~Tile();
  
  
  Sector getSector() const {
    return _sector;
  }
  
  int getLevel() const {
    return _level;
  }
  
  int getRow() const {
    return _row;
  }
  
  int getColumn() const {
    return _column;
  }
  
  void setTextureSolved(bool textureSolved) {
    _textureSolved = textureSolved;
  }
  
  bool isTextureSolved() const {
    return _textureSolved;
  }
  
  void setFallbackTextureTile(Tile* fallbackTextureTile) {
    _fallbackTextureTile = fallbackTextureTile;
  }
  
  void render(const RenderContext* rc,
              const TileTessellator* tessellator,
              TileTexturizer* texturizer,
              const TileParameters* parameters,
              TilesCache* tilesCache);
  
};

#endif
