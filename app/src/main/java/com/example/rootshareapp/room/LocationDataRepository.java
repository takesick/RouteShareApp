package com.example.rootshareapp.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.rootshareapp.model.Guide;
import com.example.rootshareapp.model.Local_Location;
import com.example.rootshareapp.model.Local_Route;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class LocationDataRepository  {
    private LocationDao locationDao;
    private RouteDao routeDao;
    private GuideDao guideDao;

    private LiveData<List<Local_Route>> mLatestRoutes;
    private LiveData<List<Local_Location>> mLatestLocations;
    private Local_Route mLocal_Route;
    private List<Local_Location> mLocationsWithinRoute;
    private LiveData<List<Guide>> mLatestGuides;

    public LocationDataRepository(Application application) {
        LocationRoomDatabase db = LocationRoomDatabase.getDatabase(application);
        routeDao = db.routeDao();
        locationDao = db.locationDao();
        guideDao = db.guideDao();
    }

    public LiveData<List<Local_Route>> getLatestRoutes() {
        mLatestRoutes = routeDao.getLatestRoutes();
        return mLatestRoutes;
    }
    public LiveData<List<Local_Location>> getLatestLocations(int route_id) {
        mLatestLocations = locationDao.getLatestLocations(route_id);
        return mLatestLocations;
    }
    public LiveData<List<Guide>> getLatestGuides(int route_id) {
        mLatestGuides = guideDao.getLatestGuides(route_id);
        return mLatestGuides;
    }

    public Long insertRoute(final Local_Route local_route) throws ExecutionException, InterruptedException {
        Future<Long> future = LocationRoomDatabase.databaseWriteExecutor.submit(new Callable<Long>() {
            public Long call() {
                return routeDao.insertRoute(local_route);
            }
        });
        return future.get();
    }

    public void updateRoute(final Local_Route local_route) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                routeDao.updateRoute(local_route);
            }
        });
    }

    public void deleteRoute(final Local_Route local_route) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                routeDao.deleteRoute(local_route);
            }
        });
    }

    public void deleteRoute(final int route_id) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                routeDao.deleteRoute(route_id);
            }
        });
    }

    public void deleteAllRoutes(final Local_Route local_route) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                routeDao.deleteAllRoutes();
            }
        });
    }

    public void insertLocation(final Local_Location local_location) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                locationDao.insertLocation(local_location);
            }
        });
    }

    public void updateLocation(final Local_Location local_location) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                locationDao.updateLocation(local_location);
            }
        });
    }

    public void deleteLocation(final Local_Location local_location) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                locationDao.deleteLocation(local_location);
            }
        });
    }

    public void deleteLocationInRoute(final int route_id) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                locationDao.deleteLocationInRoute(route_id);
            }
        });
    }

    public void deleteAllLocations() {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                locationDao.deleteAllLocations();
            }
        });
    }

    public void insertGuide(final Guide guide) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                guideDao.insertGuide(guide);
            }
        });
    }

    public void updateGuide(final Guide guide) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                guideDao.updateGuide(guide);
            }
        });
    }

    public void deleteGuide(final Guide guide) {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                guideDao.deleteGuide(guide);
            }
        });
    }

    public void deleteAllGuides() {
        LocationRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                guideDao.deleteAllGuides();
            }
        });
    }

    public List<Local_Location> getLocationsWithinRoute(Integer route_id) throws ExecutionException, InterruptedException {
        mLocationsWithinRoute = new getLocationsWithinRouteAsyncTask(locationDao).execute(route_id).get();
        return mLocationsWithinRoute;
    }

    private static class getLocationsWithinRouteAsyncTask extends AsyncTask<Integer, Void, List<Local_Location>> {
        private LocationDao locationDao;

        private getLocationsWithinRouteAsyncTask(LocationDao locationDao) {
            this.locationDao = locationDao;
        }

        @Override
        protected List<Local_Location> doInBackground(Integer... route_id) {
            return locationDao.getLocationsWithinRoute(route_id);
        }

        @Override
        protected void onPostExecute(List<Local_Location> result) {
            super.onPostExecute(result);
        }
    }

    public Local_Route getSelectedRoute(Integer route_id) throws ExecutionException, InterruptedException {
        mLocal_Route = new getSelectedRouteAsyncTask(routeDao).execute(route_id).get();
        return mLocal_Route;
    }

    private static class getSelectedRouteAsyncTask extends AsyncTask<Integer, Void, Local_Route> {
        private RouteDao routeDao;

        private getSelectedRouteAsyncTask(RouteDao routeDao) {
            this.routeDao = routeDao;
        }

        @Override
        protected Local_Route doInBackground(Integer... route_id) {
            return routeDao.getSelectedRoute(route_id);
        }

        @Override
        protected void onPostExecute(Local_Route result) {
            super.onPostExecute(result);
        }
    }

}
