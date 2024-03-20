class Timeline {

    constructor(data = []) {
        this.store = data;
        this.timeline = document.querySelector('#timeline');

        this.updateTimeline();
    }

    updateTimeline () {
        this.timeline.innerHTML = '';
        const rows = this.store.map(row => {
            return new ListItem(row).getHtmlElement()
        });
        this.timeline.append(...rows);
    }
}

class ListItem {

    listElm = null;

    constructor(data = []) {
        this.data = data;
        this.init();
    }

    init () {
        this.listElm = document.createElement('li');
        this.listElm.setAttribute('class', 'event');
        this.listElm.setAttribute('data-date', this.data.createdAt);

        const author = document.createElement('h3');
        author.append(this.data.author)
        this.listElm.append(author);

        const text = document.createElement('p');
        text.append(this.data.text)
        this.listElm.append(text);
    }

    getHtmlElement = () => this.listElm;

}