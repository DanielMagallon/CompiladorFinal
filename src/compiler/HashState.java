package compiler;


import java.io.Serializable;
import java.util.LinkedHashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class HashState implements Serializable
 {
        private static final long serialVersionUID = -113644L;
        
        private final String valorTerminal,valorNoEncontrado;

        private final int estado;

        private final LinkedHashMap<String,Integer> comp_valor = new LinkedHashMap<>();

        @Override
        public String toString() {
            return comp_valor.toString();
        }

        public HashState(String valorNoEncontrado, String valorTerminal, int estado)
        {
            this.valorTerminal = valorTerminal;
            this.valorNoEncontrado = valorNoEncontrado;
            this.estado = estado;
        }
        
        public void addCV(String comp,Integer val)
        {
            comp_valor.put(comp, val);
        }

        public Integer found(String comp)
        {
            comp = comp.toLowerCase();
            return comp_valor.get(comp);
        }
        
        public String getValorNoEncontrado() {
            return valorNoEncontrado;
        }

        public String getValorTerminal() {
            return valorTerminal;
        }

        public int getEstado() {
            return estado;
        }
        
        
        
        
    }
