/*
  Copyright 2017, Google, Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package com.example.monitoring;

// [START monitoring_quickstart]
// Imports the Google Cloud client library
import com.google.cloud.monitoring.spi.v3.MetricServiceClient;

import com.google.api.Metric;
import com.google.api.MonitoredResource;
import com.google.monitoring.v3.CreateTimeSeriesRequest;
import com.google.monitoring.v3.Point;
import com.google.monitoring.v3.ProjectName;
import com.google.monitoring.v3.TimeInterval;
import com.google.monitoring.v3.TimeSeries;
import com.google.monitoring.v3.TypedValue;
import com.google.protobuf.util.Timestamps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuickstartSample {
  public static void main(String... args) throws Exception {
    // Instantiates a client
    MetricServiceClient metricServiceClient = MetricServiceClient.create();

    // Your Google Cloud Platform project ID
    String projectId = "YOUR_PROJECT_ID";

    TimeInterval interval = TimeInterval.newBuilder()
      .setEndTime(Timestamps.fromMillis(System.currentTimeMillis()))
      .build();
    TypedValue value = TypedValue.newBuilder()
      .setDoubleValue(123.45)
      .build();

    // Prepares an individual data point
    Point point = Point.newBuilder()
      .setInterval(interval)
      .setValue(value)
      .build();

    List<Point> pointList = new ArrayList<>();
    pointList.add(point);

    ProjectName name = ProjectName.create(projectId);

    Map<String, String> metricLabels = new HashMap<String, String>();
    metricLabels.put("store_id", "Pittsburg");
    Metric metric = Metric.newBuilder()
      .setType("custom.googleapis.com/stores/daily_sales")
      .putAllLabels(metricLabels)
      .build();
    Map<String, String> resourceLabels = new HashMap<String, String>();
    resourceLabels.put("project_id", projectId);
    MonitoredResource resource = MonitoredResource.newBuilder()
      .setType("global")
      .putAllLabels(resourceLabels)
      .build();
    TimeSeries timeSeries = TimeSeries.newBuilder()
      .setMetric(metric)
      .setResource(resource)
      .addAllPoints(pointList)
      .build();

    List<TimeSeries> timeSeriesList = new ArrayList<>();
    timeSeriesList.add(timeSeries);

    // Prepares the time series request
    CreateTimeSeriesRequest request = CreateTimeSeriesRequest.newBuilder()
      .setNameWithProjectName(name)
      .addAllTimeSeries(timeSeriesList)
      .build();

    // Writes time series data
    metricServiceClient.createTimeSeries(request);

    System.out.printf("Done writing time series data.%n");
  }
}
// [END monitoring_quickstart]
