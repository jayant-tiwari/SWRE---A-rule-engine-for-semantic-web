package SWRE;

import java.util.List;

public class RuleJson {

    private List<String> rules ;

    public RuleJson() {
    }

    public RuleJson(List<String>rules){
        this.rules=rules;
    }
    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }
}
