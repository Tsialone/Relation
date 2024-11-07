class Myexception extends Exception {
    String cause;
   public  Myexception (String cuase)
    {
        super(cuase);
    }
    public void setCause(String cause) {
        this.cause = cause;
    }
}