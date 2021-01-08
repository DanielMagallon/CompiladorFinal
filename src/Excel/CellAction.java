/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import java.io.Serializable;

 public class CellAction implements Serializable
    {
        private static final long serialVersionUID = -13454L;
        
        private final String actions[];
        private final String err,rec;

        public CellAction(String[] actions, String err, String rec) {
            this.actions = actions;
            this.err = err;
            this.rec = rec;
            
        }
        
        public String[] getActions()
        {
           return actions;
        }

        public String getErr() {
            return err;
        }

        public String getRec() {
            return rec;
        }

        @Override
        public String toString() {
            return "Actions: "+actions.length+"- Err: "+err+" - Rec: "+rec;
        }
 }      
  