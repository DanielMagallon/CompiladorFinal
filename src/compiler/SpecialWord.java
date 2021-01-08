package compiler;

import java.io.Serializable;

public class SpecialWord implements Serializable
{
        public String word;
        public String definition;
        public String cCode;

        public SpecialWord(String word, String definition, String cCode) {

            this.word = word;
            this.definition = definition;
            this.cCode = cCode;
        }

        @Override
        public String toString() {
            return "SpecialWord{" +
                    "word='" + word + '\'' +
                    ", definition='" + definition + '\'' +
                    ", cCode='" + cCode + '\'' +
                    '}';
        }
    }
