package compiler;

import java.io.Serializable;

public class Symbol implements Serializable {

    public String symbol;
    public String definition;

    public Symbol(String symbol, String definition) {
        this.symbol = symbol;
        this.definition = definition;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "symbol='" + symbol + '\'' +
                ", definition='" + definition + '\'' +
                '}';
    }
}
