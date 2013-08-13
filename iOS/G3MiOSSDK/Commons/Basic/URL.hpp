//
//  URL.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_URL_hpp
#define G3MiOSSDK_URL_hpp

#include <string>
#include "Disposable.hpp"

class URL : public Disposable {
private:
  const std::string _path;
  URL& operator=(const URL& that);

public:

  URL(const URL& that) :
  _path(that._path)
  {
  }

  URL():_path("") {
  }

  /**
   Creates an URL.
   
   @param escapePath Escape the given path (true) or take it as it is given (false)
   */
  URL(const std::string& path,
      const bool escapePath) :
  _path(  escapePath ? escape(path) : path  )
  {
  }

  URL(const URL& parent,
      const std::string& path) :
  _path( parent.getPath() + "/" + path )
  {
  }

  ~URL() {
    JAVA_POST_DISPOSE
  }

  std::string getPath() const {
    return _path;
  }

  static URL nullURL() {
    return URL("__NULL__", false);
  }

  bool isNull() const {
    return (_path == "__NULL__");
  }

  bool isEqualsTo(const URL& that) const {
    return (_path == that._path);
  }
  
  static const std::string FILE_PROTOCOL;
  
  bool isFileProtocol() const;

  const std::string description() const;

  const static std::string escape(const std::string& path);

#ifdef C_CODE
  bool operator<(const URL& that) const {
    if (_path < that._path) {
      return true;
    }
    return false;
  }
#endif

#ifdef JAVA_CODE
  @Override
	public int hashCode() {
		return _path.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final URL other = (URL) obj;
    if (_path.equals(other._path)) {
      return true;
    }
    return false;
	}
#endif

};


#endif
