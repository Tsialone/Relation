import java.util.*;;
public class Condition {
    Attribut element1 ;
    Attribut element2 ;
    String operator;
    Object constante = null;
    String liaison;
    String liaisonspossibles [] = {"or" , "and"};
    List<Condition> adjoint = new ArrayList <>();
    

    public Condition (Attribut element1 , String operator, Attribut   element2 )
    {
        setElement1(element1);
        setElement2(element2);
        setOperator(operator);
    }
    public List<String> getLiaisonspossibles() {

        return Arrays.asList(liaisonspossibles);
    }
    public  Condition (Attribut element1 , String operator ,  Object constante)
    {
        setElement1(element1);
        setConstante(constante);
        setOperator(operator);
    }
    public  Condition (Attribut element1 , String operator ,  Object constante , String liaison)
    {
        setElement1(element1);
        setConstante(constante);
        setOperator(operator);
        setLiaison(liaison);
    }
    public void setElement2(Attribut element2) {
        this.element2 = element2;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public void setConstante(Object constante) {
        this.constante = constante;
    }
    public void setElement1(Attribut element1) {
        this.element1 = element1;
    }
    public Attribut getElement1() {
        return element1;
    }
    public Attribut getElement2() {
        return element2;
    }
    public Object getConstante() {
        return constante;
    }
    public String getOperator() {
        return operator;
    }
    public void and(Condition condition)
    {

    }
    public void or(Condition condition)
    {
        adjoint.add(condition);
    } 
    public void setLiaison(String liaison) {
        this.liaison = liaison;
    }
    public String getLiaison() {
        return liaison;
    }

    

}
