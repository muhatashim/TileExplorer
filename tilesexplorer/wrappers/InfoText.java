/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tilesexplorer.wrappers;

import tilesexplorer.impls.Executable;

/**
 *
 * @author VOLT
 */
public class InfoText {

    private final String string;
    private final Executable e;

    public InfoText(String string, Executable e) {
        this.string = string;
        this.e = e;
    }

    public String getString() {
        return string;
    }

    public Executable getExecutable() {
        return e;
    }

    public String toString() {
        return string;
    }
}
