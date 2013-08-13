//
//  TextureMapping.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#ifndef G3MiOSSDK_TextureMapping_hpp
#define G3MiOSSDK_TextureMapping_hpp

//#include <vector>
#include "MutableVector2D.hpp"

class IGLTextureId;

class G3MRenderContext;
class IFloatBuffer;
class GLGlobalState;
class GPUProgramState;
class GLState;

class TextureMapping {
public:
  
  virtual ~TextureMapping() {
    JAVA_POST_DISPOSE
  }
  
  virtual bool isTransparent() const = 0;
  
  virtual void modifyGLState(GLState& state) const = 0;
};



class SimpleTextureMapping : public TextureMapping {
private:
  const IGLTextureId* _glTextureId;
  
  IFloatBuffer* _texCoords;
  const bool    _ownedTexCoords;

  MutableVector2D _translation;
  MutableVector2D _scale;

  const bool _isTransparent;
  
public:
  
  SimpleTextureMapping(const IGLTextureId* glTextureId,
                       IFloatBuffer* texCoords,
                       bool ownedTexCoords,
                       bool isTransparent) :
  _glTextureId(glTextureId),
  _texCoords(texCoords),
  _translation(0, 0),
  _scale(1, 1),
  _ownedTexCoords(ownedTexCoords),
  _isTransparent(isTransparent)
  {
    
  }
  
  void setTranslationAndScale(const Vector2D& translation,
                              const Vector2D& scale) {
    _translation = translation.asMutableVector2D();
    _scale       = scale.asMutableVector2D();
  }
  
  virtual ~SimpleTextureMapping();
  
  const IGLTextureId* getGLTextureId() const {
    return _glTextureId;
  }

  IFloatBuffer* getTexCoords() const {
    return _texCoords;
  }
  
//  GLGlobalState* bind(const G3MRenderContext* rc, const GLGlobalState& parentState, GPUProgramState& progState) const;

  bool isTransparent() const {
    return _isTransparent;
  }
  
//  void modifyGLGlobalState(GLGlobalState& GLGlobalState) const;
//  
//  void modifyGPUProgramState(GPUProgramState& progState) const;
  
  void modifyGLState(GLState& state) const;

};
#endif
