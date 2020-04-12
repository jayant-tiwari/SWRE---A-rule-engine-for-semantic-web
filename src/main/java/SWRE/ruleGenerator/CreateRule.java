package SWRE.ruleGenerator;

import java.util.*;

public class CreateRule {
    private List<String>antecedent;
    private List<String>consequent;
    public CreateRule() {
    }

    public CreateRule(List<String> antecedent,List<String> consequent) {
        this.antecedent = antecedent;
       this. consequent = consequent;
    }

    public List<String> getAntecedent() {
        return antecedent;
    }

    public void setAntecedent(List<String> antecedent) {
        this.antecedent = antecedent;
    }

    public List<String> getConsequent() {
        return consequent;
    }

    public void setConsequent(List<String> consequent) {
        this.consequent = consequent;
    }


}
