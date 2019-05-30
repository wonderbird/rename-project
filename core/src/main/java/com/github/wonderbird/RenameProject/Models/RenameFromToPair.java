package com.github.wonderbird.RenameProject.Models;

public class RenameFromToPair
{
   private String from;

   private String to;

   RenameFromToPair(final String from, final String to)
   {
      this.from = from;
      this.to = to;
   }

   @Override
   public boolean equals(final Object aOther)
   {
      if(this == aOther)
      {
         return true;
      }

      if(!(aOther instanceof RenameFromToPair))
      {
         return false;
      }

      final RenameFromToPair other = (RenameFromToPair)aOther;
      final boolean result = other.from.equals(from) && other.to.equals(to);

      return result;
   }

   /**
    * Hash code according to Josh Block: Effective Java
    * @return Hash code for the current combination of from and to
    * @see <a href="https://stackoverflow.com/questions/113511/best-implementation-for-hashcode-method">StackOverflow:
    * Best implementation for hashCode method</a>
    */
   @Override
   public int hashCode()
   {
      final int PrimeNumberSeed = 37;

      int hash = PrimeNumberSeed;

      final int hashFrom = from.hashCode();
      hash = PrimeNumberSeed * hash + hashFrom;

      final int hashTo = to.hashCode();
      hash = PrimeNumberSeed * hash + hashTo;

      return hash;
   }

   public String getFrom()
   {
      return from;
   }

   public void setFrom(final String from)
   {
      this.from = from;
   }

   public String getTo()
   {
      return to;
   }

   public void setTo(final String to)
   {
      this.to = to;
   }
}
