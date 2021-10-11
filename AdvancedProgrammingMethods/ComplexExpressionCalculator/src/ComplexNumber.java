public class ComplexNumber {
    private float real;
    private float imaginary;

    public ComplexNumber(float real, float imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public float getReal() {
        return real;
    }

    public float getImaginary() {
        return imaginary;
    }

    public ComplexNumber add(ComplexNumber nr){
        this.real += nr.getReal();
        this.imaginary += nr.getImaginary();

        return this;
    }

    public ComplexNumber subtract(ComplexNumber nr){
        this.real -= nr.getReal();
        this.imaginary -= nr.getImaginary();

        return this;
    }

    public ComplexNumber multiply(ComplexNumber nr){
        float aux1, aux2;
        aux1 = this.real * nr.getReal() - this.imaginary * nr.getImaginary();
        aux2 = this.real * nr.getImaginary() + this.imaginary * nr.getReal();
        this.real = aux1;
        this.imaginary = aux2;

        return this;
    }

    public ComplexNumber divide(ComplexNumber nr){
        float aux1, aux2;
        aux1 = (this.real * nr.getReal() + this.imaginary * nr.getImaginary()) / (nr.getImaginary() * nr.getImaginary() + nr.getReal() * nr.getReal());
        aux2 = (this.imaginary * nr.getReal() - this.real * nr.getImaginary()) / (nr.getImaginary() * nr.getImaginary() + nr.getReal() * nr.getReal());
        this.real = aux1;
        this.imaginary = aux2;

        return this;
    }

    public void conjugate(){
        this.imaginary = -this.imaginary;
    }
}
