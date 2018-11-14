package dyg.beans;

import java.util.List;

public class CiBaWordBeanJson {

    /**
     * word_name : hello
     * is_CRI : 1
     * exchange : {"word_pl":["hellos"],"word_past":"","word_done":"","word_ing":"","word_third":"","word_er":"","word_est":""}
     * symbols : [{"ph_en":"hə'ləʊ","ph_am":"həˈloʊ","ph_other":"","ph_en_mp3":"","ph_am_mp3":"http://res.iciba.com/resource/amp3/1/0/5d/41/5d41402abc4b2a76b9719d911017c592.mp3","ph_tts_mp3":"http://res-tts.iciba.com/5/d/4/5d41402abc4b2a76b9719d911017c592.mp3","parts":[{"part":"int.","means":["哈喽，喂","你好，您好","表示问候","打招呼"]},{"part":"n.","means":["\u201c喂\u201d的招呼声或问候声"]},{"part":"vi.","means":["喊\u201c喂\u201d"]}]}]
     * items : [""]
     */

    private String word_name;
    private int is_CRI;
    private ExchangeBean exchange;
    private List<SymbolsBean> symbols;
    private List<String> items;

    public String getWord_name() {
        return word_name;
    }

    public void setWord_name(String word_name) {
        this.word_name = word_name;
    }

    public int getIs_CRI() {
        return is_CRI;
    }

    public void setIs_CRI(int is_CRI) {
        this.is_CRI = is_CRI;
    }

    public ExchangeBean getExchange() {
        return exchange;
    }

    public void setExchange(ExchangeBean exchange) {
        this.exchange = exchange;
    }

    public List<SymbolsBean> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<SymbolsBean> symbols) {
        this.symbols = symbols;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
    public static class ExchangeBean {
        /**
         * word_pl : ["hellos"]
         * word_past :
         * word_done :
         * word_ing :
         * word_third :
         * word_er :
         * word_est :
         */

        private List<String> word_past;
        private List<String> word_done;
        private List<String> word_ing;
        private List<String> word_third;
        private List<String> word_er;
        private List<String> word_est;
        private List<String> word_pl;

        public List<String> getWord_past() {
            return word_past;
        }

        public void setWord_past(List<String> word_past) {
            this.word_past = word_past;
        }

        public List<String> getWord_done() {
            return word_done;
        }

        public void setWord_done(List<String> word_done) {
            this.word_done = word_done;
        }

        public List<String> getWord_ing() {
            return word_ing;
        }

        public void setWord_ing(List<String> word_ing) {
            this.word_ing = word_ing;
        }

        public List<String> getWord_third() {
            return word_third;
        }

        public void setWord_third(List<String> word_third) {
            this.word_third = word_third;
        }

        public List<String> getWord_er() {
            return word_er;
        }

        public void setWord_er(List<String> word_er) {
            this.word_er = word_er;
        }

        public List<String> getWord_est() {
            return word_est;
        }

        public void setWord_est(List<String> word_est) {
            this.word_est = word_est;
        }

        public List<String> getWord_pl() {
            return word_pl;
        }

        public void setWord_pl(List<String> word_pl) {
            this.word_pl = word_pl;
        }
    }

    public static class SymbolsBean {
        /**
         * ph_en : hə'ləʊ
         * ph_am : həˈloʊ
         * ph_other :
         * ph_en_mp3 :
         * ph_am_mp3 : http://res.iciba.com/resource/amp3/1/0/5d/41/5d41402abc4b2a76b9719d911017c592.mp3
         * ph_tts_mp3 : http://res-tts.iciba.com/5/d/4/5d41402abc4b2a76b9719d911017c592.mp3
         * parts : [{"part":"int.","means":["哈喽，喂","你好，您好","表示问候","打招呼"]},{"part":"n.","means":["\u201c喂\u201d的招呼声或问候声"]},{"part":"vi.","means":["喊\u201c喂\u201d"]}]
         */

        private String ph_en;
        private String ph_am;
        private String ph_other;
        private String ph_en_mp3;
        private String ph_am_mp3;
        private String ph_tts_mp3;
        private List<PartsBean> parts;

        public String getPh_en() {
            return ph_en;
        }

        public void setPh_en(String ph_en) {
            this.ph_en = ph_en;
        }

        public String getPh_am() {
            return ph_am;
        }

        public void setPh_am(String ph_am) {
            this.ph_am = ph_am;
        }

        public String getPh_other() {
            return ph_other;
        }

        public void setPh_other(String ph_other) {
            this.ph_other = ph_other;
        }

        public String getPh_en_mp3() {
            return ph_en_mp3;
        }

        public void setPh_en_mp3(String ph_en_mp3) {
            this.ph_en_mp3 = ph_en_mp3;
        }

        public String getPh_am_mp3() {
            return ph_am_mp3;
        }

        public void setPh_am_mp3(String ph_am_mp3) {
            this.ph_am_mp3 = ph_am_mp3;
        }

        public String getPh_tts_mp3() {
            return ph_tts_mp3;
        }

        public void setPh_tts_mp3(String ph_tts_mp3) {
            this.ph_tts_mp3 = ph_tts_mp3;
        }

        public List<PartsBean> getParts() {
            return parts;
        }

        public void setParts(List<PartsBean> parts) {
            this.parts = parts;
        }

        public static class PartsBean {
            /**
             * part : int.
             * means : ["哈喽，喂","你好，您好","表示问候","打招呼"]
             */

            private String part;
            private List<String> means;

            public String getPart() {
                return part;
            }

            public void setPart(String part) {
                this.part = part;
            }

            public List<String> getMeans() {
                return means;
            }

            public void setMeans(List<String> means) {
                this.means = means;
            }
        }
    }
}
