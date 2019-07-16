class UrlQueue:
    def __init__(self):
        self.visited_url = []
        self.unvisited_url = []

    def get_visited_url(self):
        return self.visited_url

    def get_unvisited_url(self):
        return self.unvisited_url

    def add_visited_url(self, url):
        self.visited_url.append(url)

    def remove_visited_url(self, url):
        self.visited_url.remove(url)

    def pop_unvisited_url(self):
        try:
            return self.unvisited_url.pop()
        except Exception:
            return None

    def add_unvisited_url(self, url):
        if url != "" and url not in self.visited_url and url not in self.unvisited_url:
            self.unvisited_url.insert(0, url)

    def get_visited_url_count(self):
        return len(self.visited_url)

    def get_unvisited_url_count(self):
        return len(self.unvisited_url)

    def is_unvisited_url_empty(self):
        return len(self.unvisited_url) == 0

