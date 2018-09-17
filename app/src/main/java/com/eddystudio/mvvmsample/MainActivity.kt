package com.eddystudio.mvvmsample

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eddystudio.mvvmsample.model.ProductItem
import com.eddystudio.mvvmsample.repo.Repository
import com.eddystudio.mvvmsample.viewmodel.ActivityVM
import com.eddystudio.mvvmsample.viewmodel.ItemVM
import com.eddystudio.quickrecyclerviewadapterlib.QuickRecyclerViewAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  lateinit var vm: ActivityVM
  lateinit var adapter: QuickRecyclerViewAdapter<ItemVM>
  lateinit var layoutManager: GridLayoutManager
  lateinit var recyclerView: RecyclerView;

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    recyclerView = findViewById<RecyclerView>(R.id.main_recycler_view)
    vm = ViewModelProviders.of(this).get(ActivityVM::class.java)
    layoutManager = GridLayoutManager(this, 2)

    setup()
  }


  private fun setup() {

    adapter = QuickRecyclerViewAdapter(ArrayList(), R.layout.category_fragment_product_grid_rv_item, BR.vm)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = layoutManager

    vm.getEvent()
        .observeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose {
          Observable.merge(
              it.ofType(ActivityVM.Event.OnCompeleted::class.java).doOnNext { d -> onData(d.list) },
              it.ofType(ActivityVM.Event.OnError::class.java).doOnNext { e -> onError(e.throwable) }
          )
        }.subscribe()

    vm.getData()
  }

  private fun onError(throwable: Throwable) {
    Toast.makeText(this, "error: ${throwable.toString()}", Toast.LENGTH_LONG).show()

  }

  private fun onData(list: List<ItemVM>) {
    adapter.SetItemList(list)
  }

}