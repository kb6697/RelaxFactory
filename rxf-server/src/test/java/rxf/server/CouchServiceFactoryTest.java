package rxf.server;

import static junit.framework.Assert.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import one.xio.AsioVisitor;
import one.xio.HttpMethod;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CouchServiceFactoryTest {
  private static ScheduledExecutorService exec;
  @BeforeClass
  public static void setUp() throws Exception {
    BlobAntiPatternObject.DEBUG_SENDJSON = true;
    HttpMethod.killswitch = false;
    exec = Executors.newSingleThreadScheduledExecutor();
    exec.submit(new Runnable() {
      public void run() {
        AsioVisitor topLevel = new ProtocolMethodDispatch();
        try {
          HttpMethod.init(new String[]{}, topLevel, 1000);
        } catch (Exception e) {
        }
      }
    });
    CouchDriver.DbCreate.$().db(DB).to().fire().tx();
  }

  @AfterClass
  public static void tearDown() throws Exception {
    CouchDriver.DbDelete.$().db(DB).to().fire().tx();
    try {
      HttpMethod.killswitch = false;
      HttpMethod.getSelector().close();
      HttpMethod.broke = null;
      exec.shutdown();
      //Thread.sleep(4000);//more than 3 seconds, standard timeout
    } catch (Exception ignore) {}
  }


  private static final String DB = "rxf_csftest";
  public static class CSFTest {
    private String _id, _rev;

    public String model;
    public String brand;
  }

  public interface TrivialCouchService extends CouchService<CSFTest> {

  }

  @Test
  public void testCouchServiceDotFind() throws Exception {
    TrivialCouchService service = CouchServiceFactory.get(TrivialCouchService.class);


    String id = CouchDriver.DocPersist.$().db(DB).validjson("{\"model\":\"abc\",\"brand\":\"def\"}").to().fire().tx().id();

    CSFTest obj = service.find(id);
    assertNotNull(obj);
    assertEquals("abc", obj.model);
    assertEquals("def", obj.brand);
  }

  @Test
  public void testCouchServiceDotPersist() throws Exception {
    TrivialCouchService service = CouchServiceFactory.get(TrivialCouchService.class);

    CouchTx tx = service.persist(new CSFTest());

    assertNotNull(tx);
    assertTrue(tx.ok());
    assertNull(tx.getError());

    CSFTest obj = service.find(tx.id());
    assertNull(obj.brand);
    assertNull(obj.model);
    obj.brand = "Best";
    obj.model = "Sample";

    CouchTx tx2 = service.persist(obj);
    assertEquals(tx.id(), tx2.id());
    assertFalse(tx.rev().equals(tx2.rev()));

    CSFTest obj2 = service.find(tx.id());
    assertEquals("Best", obj2.brand);
    assertEquals("Sample", obj2.model);
  }
}
