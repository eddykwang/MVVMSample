package com.eddystudio.mvvmsample.service

import io.reactivex.Observable
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.AfterClass
import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


@RunWith(JUnit4::class)
class ProductServiceTest {

  private val service = retrofit.create(ProductService::class.java)

  companion object {
    private val mockServer = MockWebServer()
    lateinit var retrofit: Retrofit

    @BeforeClass
    @JvmStatic
    fun setup() {
      mockServer.start()
      mockServer.setDispatcher(object : Dispatcher() {
        override fun dispatch(request: RecordedRequest?): MockResponse {
          println(request?.path)
          if(request?.method == "GET") {
            val classLoader = javaClass.classLoader
            when {
              request.path.endsWith("/category_all.json") -> {
                val file = File(classLoader.getResource("network/category_all.json").file)
                return MockResponse().setResponseCode(200).setBody(file.readText())
              }
            }
          }
          return MockResponse().setResponseCode(400)
        }
      })

      retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(mockServer.url("/")).build()
    }

    @AfterClass
    @JvmStatic
    fun afterTest() {
      mockServer.shutdown()
    }
  }


  @Test
  fun getAllItem() {
    Observable.fromCallable { service.getItemByCategory("all").execute() }
        .map { it.body() }
        .subscribe(
            {
              for(i in 0 until it!!.size) {
                assertEquals(it[i].itemId, "m${it[i].name}")
              }
            },
            { fail("something goes wrong") }
        )
  }

  @Test
  fun getWrongItem() {
    var testFailed = false
    Observable.fromCallable { service.getItemByCategory("error").execute() }
        .map { it.body() }
        .subscribe(
            {
              testFailed = false
            },
            {
              testFailed = true
            }
        )

    assertEquals(testFailed, true)
  }
}