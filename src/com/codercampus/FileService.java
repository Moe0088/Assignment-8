package com.codercampus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class FileService {
	Assignment8 assignment = new Assignment8();
	ExecutorService executorService = Executors.newCachedThreadPool();

	public List<Integer> getAllNumbers() {
		List<CompletableFuture<List<Integer>>> futureTask = new ArrayList<>();

		for (int i = 0; i < 1000; i++) {
			CompletableFuture<List<Integer>> numberTask 
			= CompletableFuture.supplyAsync(() -> assignment.getNumbers(),
					executorService);
		futureTask.add(numberTask);
		}
		CompletableFuture<Void> completedTask =
				CompletableFuture.allOf(futureTask.toArray(new CompletableFuture[0]));
		completedTask.join();
	List<Integer> allNumbers = futureTask.stream()
			                             .flatMap(numberTask -> numberTask.join().stream())
			                             .collect(Collectors.toList());
	executorService.shutdown();

		return allNumbers;

	}

	public Map<Integer, Integer> countNumbers() {
		Map<Integer, Integer> count = new HashMap<>();

		List<Integer> numbers = getAllNumbers();
		for (Integer number : numbers) {
			if (count.containsKey(number)) {
				int currentCount = count.get(number);
				count.put(number, currentCount + 1);
			} else {
				count.put(number, 1);
			}

		}

		return count;

	}

	public void printNumberCounts() {
		Map<Integer, Integer> numberCounts = countNumbers(); 

		for (Map.Entry<Integer, Integer> entry : numberCounts.entrySet()) {

			System.out.println(entry.getKey() + "=" + entry.getValue());
		}

	}
}
