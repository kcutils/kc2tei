import kctotei.elements.AnnotationElementCollection;
import kctotei.elements.Label;
import kctotei.elements.TimeMark;
import kctotei.elements.TimedAnnotationElement;
import labels.node.ATlabel;
import labels.node.PTlabel;
import labels.node.Switch;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationElementCollectionTest {

  @Test
  public void addTest () {
    AnnotationElementCollection collection = new AnnotationElementCollection();

    Label l = new Label(new ATlabel());

    Assert.assertEquals(0, collection.getAnnotationElements().size());
    collection.add(null);
    Assert.assertEquals(0, collection.getAnnotationElements().size());
    collection.add(l);
    Assert.assertEquals(1, collection.getAnnotationElements().size());
    Assert.assertEquals(l, collection.getAnnotationElements().get(0));

  }

}
