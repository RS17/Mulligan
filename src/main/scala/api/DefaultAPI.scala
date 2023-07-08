package api

import cats.effect._
import cats.effect.unsafe.IORuntime
import cats.syntax.all._
import com.comcast.ip4s._
import com.github.rs17.mulligan.{DefaultLong, DefaultModule, DefaultString, DefaultStruct}
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe.jsonOf
import org.http4s.dsl.io.{->, /, Ok, _}
import org.http4s.dsl.request.:?
import org.http4s.ember.server._
import org.http4s.implicits._
import org.http4s.server.Router

// plan is to use http4s: https://www.growin.com/blog/three-scala-solutions-for-rest-apis/
// Note: It may seem to make sense that an API is a module rather than a struct. But a module *has* an API. A DefaultAPI
// is not an API Server - it's just the interface part. So in this case it's really just a component of a module.  To
// make a separate API Server, the server itself should be a module with an API.
// One major problem is using Kleisli makes this a major problem to debug and provide logs as to what's going on.

// Also, what is a kleisli?  Beats me. But this article is the best explanation I've found: https://sanj.ink/posts/2017-06-07-composing-monadic-functions-with-kleisli-arrows.html
class DefaultAPI(module: DefaultModule) extends DefaultStruct {
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
  lazy val endpoints: HttpRoutes[IO] = defaultGetSample <+> defaultGet <+> defaultPut <+> defaultPost <+> defaultDelete
  val apiRoot = "/api"
  val httpApp = Router(apiRoot -> endpoints).orNotFound
  val server = EmberServerBuilder
    .default[IO]
    .withHost(ipv4"0.0.0.0")
    .withPort(port"8080")
    .withHttpApp(httpApp)
    .build

  val shutdown = server.allocated.unsafeRunAndForget()
  println("API now running at " + apiRoot + path)
}
