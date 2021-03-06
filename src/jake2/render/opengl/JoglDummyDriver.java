/*
 * JoglDriver.java
 * Copyright (C) 2004
 * 
 */
/*
Copyright (C) 1997-2001 Id Software, Inc.

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

 */

package jake2.render.opengl;

import com.jogamp.nativewindow.util.Dimension;
import com.jogamp.newt.MonitorMode;
import com.jogamp.opengl.GLProfile;
import jake2.qcommon.xcommand_t;
import jake2.render.Base;

import java.util.List;

/**
 * JoglCommon
 */
public abstract class JoglDummyDriver extends DummyGL implements GLDriver {

    protected static final GLProfile glp = GLProfile.getGL2ES2(); // exception if n/a is desired
    
    protected JoglDummyDriver() {
        super();
        // singleton
    }

    private NEWTWin newtWin = null;

    public abstract String getName();
    
    public List<MonitorMode> getModeList() {
        if(null == newtWin) {
            throw new RuntimeException("NEWTWin not yet initialized.");
        }
        return newtWin.getModeList();        
    }
    
    public int setMode(Dimension dim, int mode, boolean fullscreen) {
        if(null == newtWin) {
            newtWin = new NEWTWin();
        }
        int res = newtWin.setMode(glp, dim, mode, fullscreen, getName());
        if( Base.rserr_ok == res ) {
            init(0, 0);
            
            return Base.rserr_ok;
        }
        return res;
    }

    public void shutdown() {
        if(null != newtWin) {
            newtWin.shutdown();
        }
    }

    /**
     * @return true
     */
    public boolean init(int xpos, int ypos) {
        // clear the screen
        // first buffer
        beginFrame(0.0f);
        glViewport(0, 0, newtWin.window.getWidth(), newtWin.window.getHeight());
        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        endFrame();
        // second buffer
        beginFrame(0.0f);
        glViewport(0, 0, newtWin.window.getWidth(), newtWin.window.getHeight());
        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        endFrame();
        return true;
    }

    @Override
    public boolean beginFrame(float camera_separation) {
        return activateGLContext(false);
    }

    public void endFrame() {
        newtWin.checkQuit();
        // newtWin.swapBuffers();
        // deactivate();
    }

    public void appActivate(boolean activate) {
        // do nothing
    }

    public void enableLogging(boolean enable) {
        // do nothing
    }

    public void logNewFrame() {
        // do nothing
    }

    /*
     * @see jake2.client.refexport_t#updateScreen()
     */
    public void updateScreen(xcommand_t callback) {
        callback.execute();
    }

    protected final boolean activateGLContext(boolean force) {
        return newtWin.activateGLContext(false);        
    }

    protected final void deactivateGLContext() {
        newtWin.deactivateGLContext();        
    }
    
    // --------------------------------------------------------------------------    
}
