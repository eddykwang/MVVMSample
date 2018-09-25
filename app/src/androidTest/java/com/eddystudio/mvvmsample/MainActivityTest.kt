package com.eddystudio.mvvmsample

import android.view.View
import androidx.annotation.NonNull
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.eddystudio.mvvmsample.model.ProductItem
import com.eddystudio.mvvmsample.viewmodel.ItemVM
import com.eddystudio.quickrecyclerviewadapterlib.QuickRecyclerViewBaseAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Rule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import com.eddystudio.mvvmsample.model.ItemStatus
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

  @get:Rule
  val activityTest = ActivityTestRule<MainActivity>(MainActivity::class.java)

  @Before
  fun before() {
    activityTest.activity.isTest = true
  }

  @Test
  fun showCategoryTestWithoutBadgeTest() {
    val vmList = ArrayList<ItemVM>()
    for(i in 0..40) {
      vmList.add(ItemVM(ProductItem(
          "id$i",
          "test-$i",
          "men",
          ItemStatus.ON_SALE.toString(),
          i,
          i,
          i * 10.00,
          "https://dummyimage.com/400x400/000/fff?text=name-$i"
      )))
    }

    Observable.just(vmList)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { activityTest.activity.adapter.SetItemList(it) }

    Thread.sleep(1000)

    for(i in 0..40) {
      Thread.sleep(100)
      onView(withId(R.id.main_recycler_view))
          .perform(RecyclerViewActions.scrollToPosition<QuickRecyclerViewBaseAdapter.QuickRecyclerViewHolder>(i))
          .check(
              matches(
                  atPosition(i,
                      withChild(Matchers.allOf(withId(R.id.category_fragment_product_grid_rv_item_name_tv), withText(vmList[i].productItem.name))),
                      withChild(Matchers.allOf(withId(R.id.category_fragment_product_grid_rv_item_status_img), withEffectiveVisibility(Visibility.GONE))),
                      withChild(Matchers.allOf(withId(R.id.category_fragment_product_grid_rv_item_pic_img), withEffectiveVisibility(Visibility.VISIBLE))),
                      withChild(
                          Matchers.allOf(
                              withChild(Matchers.allOf(withId(R.id.category_fragment_product_grid_rv_item_likes_tv), withText("${vmList[i].productItem.likes}"))),
                              withChild(Matchers.allOf(withId(R.id.category_fragment_product_grid_rv_item_comments_tv), withText("${vmList[i].productItem.comments}"))),
                              withChild(Matchers.allOf(withId(R.id.category_fragment_product_grid_rv_item_price_tv), withText(vmList[i].productItem.price.toString())))
                          )
                      )
                  ))
          )
    }
  }

  @Test
  fun showBadgeTest() {
    val vmList = ArrayList<ItemVM>()
    for(i in 0..40) {
      vmList.add(ItemVM(ProductItem(
          "id$i",
          "test-$i",
          "men",
          ItemStatus.SOLD_OUT.toString(),
          i,
          i,
          i * 10.00,
          "https://dummyimage.com/400x400/000/fff?text=name-$i"
      )))
    }

    Observable.just(vmList)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { activityTest.activity.adapter.SetItemList(it) }

    Thread.sleep(1000)

    for(i in 0..40) {
      Thread.sleep(100)
      onView(withId(R.id.main_recycler_view))
          .perform(RecyclerViewActions.scrollToPosition<QuickRecyclerViewBaseAdapter.QuickRecyclerViewHolder>(i))
          .check(
              matches(
                  atPosition(i,
                      withChild(Matchers.allOf(withId(R.id.category_fragment_product_grid_rv_item_name_tv), withText(vmList[i].productItem.name))),
                      withChild(Matchers.allOf(withId(R.id.category_fragment_product_grid_rv_item_status_img), withEffectiveVisibility(Visibility.VISIBLE))),
                      withChild(Matchers.allOf(withId(R.id.category_fragment_product_grid_rv_item_pic_img), withEffectiveVisibility(Visibility.VISIBLE))),
                      withChild(
                          Matchers.allOf(
                              withChild(Matchers.allOf(withId(R.id.category_fragment_product_grid_rv_item_likes_tv), withText("${vmList[i].productItem.likes}"))),
                              withChild(Matchers.allOf(withId(R.id.category_fragment_product_grid_rv_item_comments_tv), withText("${vmList[i].productItem.comments}"))),
                              withChild(Matchers.allOf(withId(R.id.category_fragment_product_grid_rv_item_price_tv), withText(vmList[i].productItem.price.toString())))
                          )
                      )
                  ))
          )
    }
  }
}

fun atPosition(position: Int, @NonNull vararg itemMatcher: Matcher<View>): Matcher<View> {
  checkNotNull(itemMatcher)
  return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
    override fun describeTo(description: Description) {
      for(item in itemMatcher) {
        description.appendText("has item at position $position\n")
        item.describeTo(description)
      }
    }

    override fun matchesSafely(view: RecyclerView): Boolean {
      val viewHolder = view.findViewHolderForAdapterPosition(position)
          ?: // has no item on such position
          return false
      return itemMatcher.all {
        it.matches(viewHolder.itemView)
      }
    }
  }
}