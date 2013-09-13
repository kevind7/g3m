//
//  CompositeTileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 9/12/13.
//
//

#ifndef __G3MiOSSDK__CompositeTileRasterizer__
#define __G3MiOSSDK__CompositeTileRasterizer__

#include "CanvasTileRasterizer.hpp"
#include <vector>

class CompositeTileRasterizer : public CanvasTileRasterizer {
private:
  std::vector<TileRasterizer*> _children;
  
public:
  ~CompositeTileRasterizer();

  std::string getId() const;

  void rasterize(const IImage* image,
                 const TileRasterizerContext& trc,
                 IImageListener* listener,
                 bool autodelete) const;

  void addTileRasterizer(TileRasterizer* tileRasterizer);

};

#endif