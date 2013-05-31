

package org.glob3.mobile.generated;

//
//  GPUProgram.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//

//
//  GPUProgram.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//


//class IFloatBuffer;

//class GL;

public class GPUProgram {

   //INativeGL* _nativeGL;
   private int                                           _programID;
   private boolean                                       _programCreated;
   private final java.util.HashMap<String, GPUAttribute> _attributes = new java.util.HashMap<String, GPUAttribute>();
   private final java.util.HashMap<String, GPUUniform>   _uniforms   = new java.util.HashMap<String, GPUUniform>();
   private String                                        _name;


   private boolean compileShader(final GL gl,
                                 final int shader,
                                 final String source) {
      final boolean result = gl.compileShader(shader, source);

      ///#if defined(DEBUG)
      //  _nativeGL->printShaderInfoLog(shader);
      ///#endif

      if (result) {
         gl.attachShader(_programID, shader);
      }
      else {
         ILogger.instance().logError("GPUProgram: Problem encountered while compiling shader.");
      }

      return result;
   }


   private boolean linkProgram(final GL gl) {
      final boolean result = gl.linkProgram(_programID);
      ///#if defined(DEBUG)
      //  _nativeGL->printProgramInfoLog(_programID);
      ///#endif
      return result;
   }


   private void deleteShader(final GL gl,
                             final int shader) {
      if (!gl.deleteShader(shader)) {
         ILogger.instance().logError("GPUProgram: Problem encountered while deleting shader.");
      }
   }


   private void getVariables(final GL gl) {

      //Uniforms
      int n = gl.getProgramiv(this, GLVariable.activeUniforms());
      for (int i = 0; i < n; i++) {
         final GPUUniform u = gl.getActiveUniform(this, i);
         if (u != null) {
            _uniforms.put(u.getName(), u);
         }
      }

      //Attributes
      n = gl.getProgramiv(this, GLVariable.activeAttributes());
      for (int i = 0; i < n; i++) {
         final GPUAttribute a = gl.getActiveAttribute(this, i);
         if (a != null) {
            _attributes.put(a.getName(), a);
         }
      }

   }


   private GPUProgram() {
   }


   public static GPUProgram createProgram(final GL gl,
                                          final String name,
                                          final String vertexSource,
                                          final String fragmentSource) {

      final GPUProgram p = new GPUProgram();

      p._name = name;

      p._programCreated = false;
      //p->_nativeGL = gl->getNative();
      p._programID = gl.createProgram();

      // compile vertex shader
      final int vertexShader = gl.createShader(ShaderType.VERTEX_SHADER);
      if (!p.compileShader(gl, vertexShader, vertexSource)) {
         ILogger.instance().logError("GPUProgram: ERROR compiling vertex shader\n");
         p.deleteShader(gl, vertexShader);
         p.deleteProgram(gl, p._programID);
         ILogger.instance().logError("GPUProgram: ERROR compiling vertex shader");
         return null;
      }

      System.out.printf("%s", vertexSource);

      // compile fragment shader
      final int fragmentShader = gl.createShader(ShaderType.FRAGMENT_SHADER);
      if (!p.compileShader(gl, fragmentShader, fragmentSource)) {
         ILogger.instance().logError("GPUProgram: ERROR compiling fragment shader\n");
         p.deleteShader(gl, fragmentShader);
         p.deleteProgram(gl, p._programID);
         ILogger.instance().logError("GPUProgram: ERROR compiling fragment shader");
         return null;
      }

      gl.bindAttribLocation(p, 0, "Position");

      // link program
      if (!p.linkProgram(gl)) {
         ILogger.instance().logError("GPUProgram: ERROR linking graphic program\n");
         p.deleteShader(gl, vertexShader);
         p.deleteShader(gl, fragmentShader);
         p.deleteProgram(gl, p._programID);
         ILogger.instance().logError("GPUProgram: ERROR linking graphic program");
         return null;
      }

      // free shaders
      p.deleteShader(gl, vertexShader);
      p.deleteShader(gl, fragmentShader);

      p.getVariables(gl);

      return p;
   }


   public void dispose() {
   }


   public final String getName() {
      return _name;
   }


   public final int getProgramID() {
      return _programID;
   }


   public final boolean isCreated() {
      return _programCreated;
   }


   public final void deleteProgram(final GL gl,
                                   final int p) {
      if (!gl.deleteProgram(p)) {
         ILogger.instance().logError("GPUProgram: Problem encountered while deleting program.");
      }
      _programCreated = false;
   }


   public final int getGPUAttributesNumber() {
      return _attributes.size();
   }


   public final int getGPUUniformsNumber() {
      return _uniforms.size();
   }


   public final GPUUniform getGPUUniform(final String name) {
      return _uniforms.get(name);
   }


   public final GPUAttribute getGPUAttribute(final String name) {
      return _attributes.get(name);
   }


   public final GPUUniformBool getGPUUniformBool(final String name) {
      final GPUUniform u = getGPUUniform(name);
      if ((u != null) && (u.getType() == GLType.glBool())) {
         return (GPUUniformBool) u;
      }
      else {
         return null;
      }
   }


   public final GPUUniformVec2Float getGPUUniformVec2Float(final String name) {
      final GPUUniform u = getGPUUniform(name);
      if ((u != null) && (u.getType() == GLType.glVec2Float())) {
         return (GPUUniformVec2Float) u;
      }
      else {
         return null;
      }
   }


   public final GPUUniformVec4Float getGPUUniformVec4Float(final String name) {
      final GPUUniform u = getGPUUniform(name);
      if ((u != null) && (u.getType() == GLType.glVec4Float())) {
         return (GPUUniformVec4Float) u;
      }
      else {
         return null;
      }
   }


   public final GPUUniformFloat getGPUUniformFloat(final String name) {
      final GPUUniform u = getGPUUniform(name);
      if ((u != null) && (u.getType() == GLType.glFloat())) {
         return (GPUUniformFloat) u;
      }
      else {
         return null;
      }
   }


   public final GPUUniformMatrix4Float getGPUUniformMatrix4Float(final String name) {
      final GPUUniform u = getGPUUniform(name);
      if ((u != null) && (u.getType() == GLType.glMatrix4Float())) {
         return (GPUUniformMatrix4Float) u;
      }
      else {
         return null;
      }
   }


   public final GPUAttributeVec1Float getGPUAttributeVec1Float(final String name) {
      final GPUAttributeVec1Float a = (GPUAttributeVec1Float) getGPUAttribute(name);
      if ((a != null) && (a.getSize() == 1) && (a.getType() == GLType.glFloat())) {
         return a;
      }
      else {
         return null;
      }
   }


   public final GPUAttributeVec2Float getGPUAttributeVec2Float(final String name) {
      final GPUAttributeVec2Float a = (GPUAttributeVec2Float) getGPUAttribute(name);
      if ((a != null) && (a.getSize() == 2) && (a.getType() == GLType.glFloat())) {
         return a;
      }
      else {
         return null;
      }
   }


   public final GPUAttributeVec3Float getGPUAttributeVec3Float(final String name) {
      final GPUAttributeVec3Float a = (GPUAttributeVec3Float) getGPUAttribute(name);
      if ((a != null) && (a.getSize() == 3) && (a.getType() == GLType.glFloat())) {
         return a;
      }
      else {
         return null;
      }
   }


   public final GPUAttributeVec4Float getGPUAttributeVec4Float(final String name) {
      final GPUAttributeVec4Float a = (GPUAttributeVec4Float) getGPUAttribute(name);
      if ((a != null) && (a.getSize() == 4) && (a.getType() == GLType.glFloat())) {
         return a;
      }
      else {
         return null;
      }
   }


   /**
    * Must be called when the program is used
    */
   public final void onUsed() {
      //  ILogger::instance()->logInfo("GPUProgram %s being used", _name.c_str());
   }


   /**
    * Must be called when the program is no longer used
    */
   public final void onUnused() {
      final GPUUniform[] uni = (GPUUniform[]) _uniforms.values().toArray();
      for (final GPUUniform element : uni) {
         element.unset();
      }

      final GPUAttribute[] att = (GPUAttribute[]) _attributes.values().toArray();
      for (int i = 0; i < uni.length; i++) {
         att[i].unset();
      }

      //ILogger::instance()->logInfo("GPUProgram %s unused", _name.c_str());
      final Iterator it = _uniforms.entrySet().iterator();
      while (it.hasNext()) {
         final Map.Entry pairs = (Map.Entry) it.next();
         final GPUUniform u = (GPUUniform) pairs.getValue();
         u.unset();
      }

      final Iterator it2 = _attributes.entrySet().iterator();
      while (it2.hasNext()) {
         final Map.Entry pairs = (Map.Entry) it2.next();
         final GPUAttribute a = (GPUAttribute) pairs.getValue();
         a.unset();
      }
   }


   /**
    * Must be called before drawing to apply Uniforms and Attributes new values
    */
   public final void applyChanges(final GL gl) {
      final GPUUniform[] uni = (GPUUniform[]) _uniforms.values().toArray();
      for (final GPUUniform u : uni) {
         if (u.wasSet()) {
            u.applyChanges(gl);
         }
         else {
            ILogger.instance().logError("Uniform " + u.getName() + " was not set.");
         }
      }

      final GPUAttribute[] att = (GPUAttribute[]) _attributes.values().toArray();
      for (final GPUAttribute a : att) {
         if (a.wasSet()) {
            a.applyChanges(gl);
         }
         else {
            if (a.isEnabled()) {
               ILogger.instance().logError("Attribute " + a.getName() + " was not set but it is enabled.");
            }
         }
      }


      //ILogger::instance()->logInfo("GPUProgram %s applying changes", _name.c_str());
      final Iterator it = _uniforms.entrySet().iterator();
      while (it.hasNext()) {
         final Map.Entry pairs = (Map.Entry) it.next();
         final GPUUniform u = (GPUUniform) pairs.getValue();
         if (u.wasSet()) {
            u.applyChanges(gl);
         }
         else {
            ILogger.instance().logError("Uniform " + u.getName() + " was not set.");
         }
      }

      final Iterator it2 = _attributes.entrySet().iterator();
      while (it2.hasNext()) {
         final Map.Entry pairs = (Map.Entry) it2.next();
         final GPUAttribute a = (GPUAttribute) pairs.getValue();
         if (a.wasSet()) {
            a.applyChanges(gl);
         }
         else {
            if (a.isEnabled()) {
               ILogger.instance().logError("Attribute " + a.getName() + " was not set but it is enabled.");
            }
         }
      }

   }


   public final GPUUniform getUniformOfType(final String name,
                                            final int type) {
      GPUUniform u = null;
      if (type == GLType.glBool()) {
         u = getGPUUniformBool(name);
      }
      else {
         if (type == GLType.glVec2Float()) {
            u = getGPUUniformVec2Float(name);
         }
         else {
            if (type == GLType.glVec4Float()) {
               u = getGPUUniformVec4Float(name);
            }
            else {
               if (type == GLType.glFloat()) {
                  u = getGPUUniformFloat(name);
               }
               else if (type == GLType.glMatrix4Float()) {
                  u = getGPUUniformMatrix4Float(name);
               }
            }
         }
      }
      return u;
   }

   /*
    void setUniform(GL* gl, const std::string& name, const Vector2D& v) const{
    Uniform* u = getUniform(name);
    if (u != NULL && u->getType() == GLType::glVec2Float()) {
    ((UniformVec2Float*)u)->set(gl, v);
    } else{
    throw G3MError("Error setting Uniform " + name);
    }
    }
    
    void setUniform(GL* gl, const std::string& name, double x, double y, double z, double w) const{
    Uniform* u = getUniform(name);
    if (u != NULL && u->getType() == GLType::glVec4Float()) {
    ((UniformVec4Float*)u)->set(gl, x,y,z,w);
    } else{
    throw G3MError("Error setting Uniform " + name);
    }
    }
    
    void setUniform(GL* gl, const std::string& name, bool b) const{
    Uniform* u = getUniform(name);
    if (u != NULL && u->getType() == GLType::glBool()) {
    ((UniformBool*)u)->set(gl, b);
    } else{
    throw G3MError("Error setting Uniform " + name);
    }
    }
    
    void setUniform(GL* gl, const std::string& name, float f) const{
    Uniform* u = getUniform(name);
    if (u != NULL && u->getType() == GLType::glFloat()) {
    ((UniformFloat*)u)->set(gl, f);
    } else{
    throw G3MError("Error setting Uniform " + name);
    }
    }
    
    void setUniform(GL* gl, const std::string& name, const MutableMatrix44D& m) const{
    Uniform* u = getUniform(name);
    if (u != NULL && u->getType() == GLType::glMatrix4Float()) {
    ((UniformMatrix4Float*)u)->set(gl, m);
    } else{
    throw G3MError("Error setting Uniform " + name);
    }
    }
    */


}