public class Domaine {
    String[] valabletype = { "VARCHAR", "NUMBER", "DECIMAL", "null" };
    String type;
    Integer limit = null;
    Integer decimallimit = null;

    public Domaine(String type) throws Myexception {
        isValableType(type);
    }

    public Domaine(String type, int limit) throws Myexception {
        this.limit = limit;
        isValableType(type);

    }

    public Integer getDecimallimit() {
        return decimallimit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void isValableType(String type) throws Myexception {
        boolean find = false;
        for (String tpe : valabletype) {
            if (tpe.equalsIgnoreCase(type)) {
                this.type = type;
                find = true;
            }
        }
        if (!find) {
            throw new Myexception("Type or name of column not found for: " + type);
        }
    }

    public static String quelType(Object objet) {
        String[] valabletype = { "VARCHAR", "NUMBER", "DECIMAL", "null" };
        try {
            String classname = objet.getClass().getSimpleName();
            if (classname.equalsIgnoreCase("string")) {
                return valabletype[0];
            } else if (classname.equalsIgnoreCase("integer")) {
                return valabletype[1];
            } else if (classname.equalsIgnoreCase("double")) {
                return valabletype[2];
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
       
        return valabletype[3];
    }

    public String getType() {
        return type;
    }

}
