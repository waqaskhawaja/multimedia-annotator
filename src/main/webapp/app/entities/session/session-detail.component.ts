import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';
import { EmbedVideoService } from 'ngx-embed-video';

import { ISession } from 'app/shared/model/session.model';

@Component({
    selector: 'jhi-session-detail',
    templateUrl: './session-detail.component.html'
})
export class SessionDetailComponent implements OnInit {
    session: ISession;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute, private embedService: EmbedVideoService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ session }) => {
            this.session = session;
        });
    }

    embedVideo(url) {
        return this.embedService.embed(url);
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
