package com.github.wonderbird.RenameProject.Models;

public class RenameFromToPair {
    private String from;

    private String to;

    RenameFromToPair(String from, String to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object aOther) {
        if (this == aOther) {
            return true;
        }

        if (!(aOther instanceof RenameFromToPair)) {
            return false;
        }

        RenameFromToPair other = (RenameFromToPair) aOther;
        boolean result = other.from.equals(from)
                && other.to.equals(to);

        return result;
    }

    /**
     * Hash code according to Josh Block: Effective Java
     *
     * @see <a href="https://stackoverflow.com/questions/113511/best-implementation-for-hashcode-method">StackOverflow: Best implementation for hashCode method</a>
     * @return Hash code for the current combination of from and to
     */
    @Override
    public int hashCode() {
        final int PrimeNumberSeed = 37;

        int hash = PrimeNumberSeed;

        int hashFrom = from.hashCode();
        hash = PrimeNumberSeed * hash + hashFrom;

        int hashTo = to.hashCode();
        hash = PrimeNumberSeed * hash + hashTo;

        return hash;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
