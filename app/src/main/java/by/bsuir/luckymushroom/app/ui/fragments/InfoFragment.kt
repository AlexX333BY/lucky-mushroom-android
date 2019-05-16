package by.bsuir.luckymushroom.app.ui.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.bsuir.luckymushroom.R
import by.bsuir.luckymushroom.app.App
import by.bsuir.luckymushroom.app.dto.articles.Article
import by.bsuir.luckymushroom.app.ui.adapters.ArticlesRecyclerViewAdapter
import by.bsuir.luckymushroom.app.ui.activities.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var articles: Array<Article>? = App.defaultArticles
        var location: Location?

        val locationManager: LocationManager =
            activity?.getSystemService(
                Context.LOCATION_SERVICE
            ) as LocationManager

//        val locationListener = object : LocationListener {
//            override fun onLocationChanged(location: Location?) {
//                location?.let {
//                    App.articlesService.getArticles(
//                        it.latitude.toInt(), it.longitude.toInt()
//                    ).enqueue(object : Callback<Array<Article>> {
//                        override fun onFailure(
//                            call: Call<Array<Article>>,
//                            t: Throwable
//                        ) {
//
//                        }
//
//                        override fun onResponse(
//                            call: Call<Array<Article>>,
//                            response: Response<Array<Article>>
//                        ) {
//                            if (response.isSuccessful) {
//                                articles = response.body()
//                            }
//                        }
//
//                    })
//                }
//            }
//
//            override fun onStatusChanged(
//                provider: String?,
//                status: Int,
//                extras: Bundle?
//            ) {
//
//            }
//
//            override fun onProviderEnabled(provider: String?) {
//
//            }
//
//            override fun onProviderDisabled(provider: String?) {
//
//            }
//
//        }

        if ((activity as MainActivity).locationPermission) {
            if (ContextCompat.checkSelfPermission(
                        activity!!,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    != PackageManager.PERMISSION_GRANTED
            ) {
//            locationManager.requestLocationUpdates(
//                LocationManager.NETWORK_PROVIDER, 0, 0f,
//                locationListener
////            )
                location = locationManager.getLastKnownLocation(
                    LocationManager.GPS_PROVIDER
                )
                App.articlesService.getArticles(
                    location.latitude.toInt(), location.longitude.toInt()
                ).enqueue(object : Callback<Array<Article>> {
                    override fun onFailure(
                        call: Call<Array<Article>>,
                        t: Throwable
                    ) {

                    }

                    override fun onResponse(
                        call: Call<Array<Article>>,
                        response: Response<Array<Article>>
                    ) {
                        if (response.isSuccessful) {
                            articles = response.body()
                        }
                    }

                })

            }
        }

        return inflater.inflate(R.layout.fragment_info, container, false)
            .apply {
                articles?.let {
                    viewManager = LinearLayoutManager(activity)
                    viewAdapter =
                        ArticlesRecyclerViewAdapter(
                            it
                        )
                    recyclerView =
                        findViewById<RecyclerView>(R.id.recyclerview).apply {
                            setHasFixedSize(true)
                            layoutManager = viewManager
                            adapter = viewAdapter
                        }
                }

            }


    }


}
