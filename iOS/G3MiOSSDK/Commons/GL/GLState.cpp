//
//  GLState.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//

#include "GLState.hpp"

GLGlobalState GLState::_currentGPUGlobalState;
GPUProgram* GLState::_currentGPUProgram = NULL;

void GLState::applyGlobalStateOnGPU(GL* gl){
  _globalState->applyChanges(gl, _currentGPUGlobalState);
}

void GLState::applyOnGPU(GL* gl, GPUProgramManager& progManager) {
  
  applyGlobalStateOnGPU(gl);
  setProgramState(gl, progManager);
  
}

void GLState::setProgramState(GL* gl, GPUProgramManager& progManager) {
  GPUProgram* prog = NULL;
  if (!_programState->isLinkedToProgram()) {
    prog = progManager.getProgram(*_programState);
    _programState->linkToProgram(*prog);
  } else{
    prog = _programState->getLinkedProgram();
  }
  
  if (prog != NULL){
    if (prog != _currentGPUProgram){
      if (_currentGPUProgram != NULL){
        _currentGPUProgram->onUnused();
      }
      _currentGPUProgram = prog;
      gl->useProgram(prog);
    }
    
    _programState->applyChanges(gl);
  } else{
    ILogger::instance()->logError("No available GPUProgram for this state.");
  }
}