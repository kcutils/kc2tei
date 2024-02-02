package kctotei.elements;

/**
 * A stress is either a primary stress or a secondary stress.
 *
 * It's a primary stress by default.
 */
public class StressType {
  private boolean isPrimaryStress;

  public StressType () {
    this.setPrimaryStress(true);
  }

  private boolean getPrimaryStress () {
    return isPrimaryStress;
  }

  public void setPrimaryStress (boolean primaryStress) {
    isPrimaryStress = primaryStress;
  }

  public StressType setPrimaryStressAndReturnStressType (boolean primaryStress) {
    isPrimaryStress = primaryStress;
    return this;
  }

  public void setIsPrimary () {
    this.setPrimaryStress(true);
  }

  public void setIsSecondary () {
    this.setPrimaryStress(false);
  }

  public boolean isPrimary () {
    return this.getPrimaryStress();
  }

  public boolean isSecondary () {
    return !this.getPrimaryStress();
  }
}
