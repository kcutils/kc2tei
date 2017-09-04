package kctotei.elements;

/**
 * A stress is either a primary stress or a secondary stress.
 *
 * It's a primary stress by default.
 */
public class StressType {
  private Boolean isPrimaryStress;

  public StressType () {
    this.setPrimaryStress(true);
  }

  private Boolean getPrimaryStress () {
    return isPrimaryStress;
  }

  public void setPrimaryStress (Boolean primaryStress) {
    isPrimaryStress = primaryStress;
  }

  public StressType setPrimaryStressAndReturnStressType (Boolean primaryStress) {
    isPrimaryStress = primaryStress;
    return this;
  }

  public void setIsPrimary () {
    this.setPrimaryStress(true);
  }

  public void setIsSecondary () {
    this.setPrimaryStress(false);
  }

  public Boolean isPrimary () {
    return this.getPrimaryStress();
  }

  public Boolean isSecondary () {
    return !this.getPrimaryStress();
  }
}
