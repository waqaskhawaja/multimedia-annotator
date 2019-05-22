/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MaTestModule } from '../../../test.module';
import { AnalysisSessionDeleteDialogComponent } from 'app/entities/analysis-session/analysis-session-delete-dialog.component';
import { AnalysisSessionService } from 'app/entities/analysis-session/analysis-session.service';

describe('Component Tests', () => {
    describe('AnalysisSession Management Delete Component', () => {
        let comp: AnalysisSessionDeleteDialogComponent;
        let fixture: ComponentFixture<AnalysisSessionDeleteDialogComponent>;
        let service: AnalysisSessionService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [AnalysisSessionDeleteDialogComponent]
            })
                .overrideTemplate(AnalysisSessionDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AnalysisSessionDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AnalysisSessionService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
