import cats.effect._
import org.http4s._
import org.http4s.dsl.io.{->, /, Ok, _}
import cats.effect.unsafe.IORuntime
import org.http4s.ember.server._
import org.http4s.server.Router
import org.http4s.implicits._
import com.comcast.ip4s._
import cats.syntax.all._
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityDecoder._
import io.circe._
import io.circe.literal._
import org.http4s.Uri.Path
import org.http4s.Uri.Path.Segment
import org.http4s.UriTemplate.PathElm
import org.http4s.circe.jsonOf

// plan is to use http4s: https://www.growin.com/blog/three-scala-solutions-for-rest-apis/
// Note: It may seem to make sense that an API is a module rather than a struct. But a module *has* an API. A DefaultAPI
// is not an API Server - it's just the interface part. So in this case it's really just a component of a module.  To
// make a separate API Server, the server itself should be a module with an API.
class DefaultAPI(module: DefaultModule) extends DefaultStruct{
  implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global
  // root path is <host>:8080/api/<modulekey>
  val path = Root.addSegment(module.key.getOrElse(DefaultString("")).toString)

  // would be nice to add limits and pagination at some point
  object OptionalKeyParamDecoderMatcher
    extends OptionalQueryParamDecoderMatcher[String]("key")
  val defaultGet = HttpRoutes.of[IO] {
    case GET -> path :? OptionalKeyParamDecoderMatcher(maybeKey)=>
      maybeKey match {
        case Some(key) => Ok(module.get(DefaultLong(key.toLong)).toString)
        case None => Ok(module.get().toString)
      }
  }

  val defaultGetSample = HttpRoutes.of[IO] {
    case GET -> path / "sample" =>
      Ok(module.getJsonSamples().toString)
  }
  val defaultPut = HttpRoutes.of[IO] {
    case PUT -> path / key / json =>
      Ok(module.put(DefaultLong(key.toLong), new DefaultString(json)).toString)
  }

  implicit val userDecoder = jsonOf[IO, DefaultString]
  val defaultPost = HttpRoutes.of[IO] {
    case req @ POST -> path / json =>
      for{
        defaultString <- req.as[DefaultString]
        resp <- Ok(module.post(defaultString).toString)
      } yield resp
  }
  val defaultDelete = HttpRoutes.of[IO] {
    case DELETE -> path / json =>
      Ok(module.delete(new DefaultString(json)).toString)
  }

  // Note: Order here matters! Will try to match first ones first
  val services = defaultGetSample <+> defaultGet <+> defaultPut <+> defaultPost <+> defaultDelete
  val apiRoot = "/api"
  val httpApp = Router(apiRoot -> services).orNotFound
  val server = EmberServerBuilder
    .default[IO]
    .withHost(ipv4"0.0.0.0")
    .withPort(port"8080")
    .withHttpApp(httpApp)
    .build

  val shutdown = server.allocated.unsafeRunAndForget()
  println("API now running at " + apiRoot + path)
}
