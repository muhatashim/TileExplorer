/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tilesexplorer.executables;

import javax.swing.SwingUtilities;
import tilesexplorer.guis.FlagExplorerUI;
import tilesexplorer.impls.Executable;

/**
 *
 * @author VOLT
 */
public class FlagExplorer implements Executable {

    private final int flag;

    public FlagExplorer(int flag) {
        this.flag = flag;
    }

    @Override
    public void execute() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FlagExplorerUI(flag).setVisible(true);
            }
        });
    }
}
